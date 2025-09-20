package xyz.sl.misic1;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.SonicAudioProcessor;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.audio.DefaultAudioSink;
import androidx.media3.ui.PlayerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.IOException;
import java.util.List;

import xyz.sl.dsp.juce.EqualizerFrequency;
import xyz.sl.dsp.juce.JuceNativeInterface;
import xyz.sl.misic1.databinding.ActivityMainBinding;

@UnstableApi public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("misic1");
    }


    private SeekBar[] seekBars;
    private TextView[] textViews;
    private static final int MIN_VALUE = -16;
    private static final int MAX_VALUE = 16;
    private static final int DEFAULT_VALUE = 0;
    private static final int RANGE = MAX_VALUE - MIN_VALUE;

    private ActivityMainBinding binding;
    private MyNativeProcessor my = new MyNativeProcessor();
    private SonicAudioProcessor sonicA = new SonicAudioProcessor();
    private JuceNativeInterface j = JuceNativeInterface.getJuceNativeInterface();

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSeekBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        RenderersFactory renderersFactory = new DefaultRenderersFactory(this) {
            @Override
            protected AudioSink buildAudioSink(@NonNull Context context,
                                               boolean enableFloatOutput,
                                               boolean enableAudioTrackPlaybackParams
            ) {
                return new DefaultAudioSink.Builder(context)
                        .setAudioProcessors(new AudioProcessor[]{ my })
                        .build();
            }
        };
        player = new ExoPlayer.Builder(this, renderersFactory)
                .build();

        playerView = findViewById(R.id.player_view);
        playerView.setPlayer(player);
        button = findViewById(R.id.button3);
        button.setOnClickListener((view)->{
            speed+=0.1f;
            sonicA.setSpeed(speed);
            player.seekTo(player.getCurrentPosition()); // 强制刷新 pipeline
            Toast.makeText(this, "speed " + speed, Toast.LENGTH_SHORT).show();
        });



//        Uri audioUri = Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
//        Uri audioUri = Uri.fromFile(new File(getExternalFilesDir(null), "SoundHelix-Song-1.mp3"));
//        Uri audioUri = Uri.parse("asset:///SoundHelix-Song-1.mp3");
//        https://cdn.pixabay.com/download/audio/2024/07/08/audio_9d9d65fe53.mp3?filename=-222787.mp3
        Uri audioUri = Uri.parse("asset:///-222787.mp3");
        Log.i(this.getClass().getName(), String.valueOf(audioUri));
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        player.setMediaItem(mediaItem);
        player.prepare();
//        player.play(); // 自动播放
        Log.i("Main", "Running start ...");
    }

    private void initSeekBar() {
        // 初始化SeekBar数组
        seekBars = new SeekBar[]{
                findViewById(R.id.seekBar2),
                findViewById(R.id.seekBar3),
                findViewById(R.id.seekBar4),
                findViewById(R.id.seekBar5),
                findViewById(R.id.seekBar6),
                findViewById(R.id.seekBar7),
                findViewById(R.id.seekBar8),
                findViewById(R.id.seekBar9),
                findViewById(R.id.seekBar10),
                findViewById(R.id.seekBar11)
        };

        // 初始化TextView数组
        textViews = new TextView[]{
                findViewById(R.id.tvSeekBar2),
                findViewById(R.id.tvSeekBar3),
                findViewById(R.id.tvSeekBar4),
                findViewById(R.id.tvSeekBar5),
                findViewById(R.id.tvSeekBar6),
                findViewById(R.id.tvSeekBar7),
                findViewById(R.id.tvSeekBar8),
                findViewById(R.id.tvSeekBar9),
                findViewById(R.id.tvSeekBar10),
                findViewById(R.id.tvSeekBar11)
        };

        // 设置每个SeekBar
        for (int i = 0; i < seekBars.length; i++) {
            setupSeekBar(seekBars[i], textViews[i], i);
        }
    }

    private void setupSeekBar(SeekBar seekBar, TextView textView, int index) {
        // 设置最大进度（范围从0到32，因为-16到16共33个值）
        seekBar.setMax(RANGE);

        // 设置初始进度（0对应-16，16对应0，32对应16）
        int initialProgress = DEFAULT_VALUE - MIN_VALUE;
        seekBar.setProgress(initialProgress);

        // 更新TextView显示
        updateTextView(textView, index, DEFAULT_VALUE);

        // 设置监听器
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 将进度转换为实际值（-16到16）
                int actualValue = progress + MIN_VALUE;
                updateTextView(textView, index, actualValue);

                // 这里可以添加您的业务逻辑
                // 例如：applyValueToSystem(index, actualValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EqualizerFrequency frequencyIndex = EqualizerFrequency.FREQUENCY_31;
                // 用户停止拖动时调用
                switch (index){
                    case 1: frequencyIndex = EqualizerFrequency.FREQUENCY_62; break;
                    case 2: frequencyIndex = EqualizerFrequency.FREQUENCY_125; break;
                    case 3: frequencyIndex = EqualizerFrequency.FREQUENCY_250; break;
                    case 4: frequencyIndex = EqualizerFrequency.FREQUENCY_500; break;
                    case 5: frequencyIndex = EqualizerFrequency.FREQUENCY_1k; break;
                    case 6: frequencyIndex = EqualizerFrequency.FREQUENCY_2k; break;
                    case 7: frequencyIndex = EqualizerFrequency.FREQUENCY_4k; break;
                    case 8: frequencyIndex = EqualizerFrequency.FREQUENCY_8k; break;
                    case 9: frequencyIndex = EqualizerFrequency.FREQUENCY_16k; break;
                    default:
                }
                float gain = seekBar.getProgress() - MAX_VALUE;
                my.setEQGain(frequencyIndex.frequency, frequencyIndex.index, gain);
                Toast.makeText(seekBar.getContext(), ("增益 "+frequencyIndex.frequency+"Hz: "+gain), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTextView(TextView textView, int index, int value) {
        textView.setText(String.format("控制项 %d: %d dB", index, value));
    }

    // 获取指定SeekBar的当前值
    public int getSeekBarValue(int seekBarIndex) {
        if (seekBarIndex >= 0 && seekBarIndex < seekBars.length) {
            return seekBars[seekBarIndex].getProgress() + MIN_VALUE;
        }
        return DEFAULT_VALUE;
    }

    // 设置指定SeekBar的值
    public void setSeekBarValue(int seekBarIndex, int value) {
        if (seekBarIndex >= 0 && seekBarIndex < seekBars.length) {
            // 确保值在范围内
            int clampedValue = Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
            seekBars[seekBarIndex].setProgress(clampedValue - MIN_VALUE);
        }
    }

    PlayerView playerView;
    ExoPlayer player;
    Button button;
    float speed = 1.0f;

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}