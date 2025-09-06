package xyz.sl.misic1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
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
import java.util.List;

import xyz.sl.misic1.databinding.ActivityMainBinding;

@UnstableApi public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("misic1");
    }
    public native void init(int sampleRate);
    public native void process(float[] audioBuffer, int numFrames, int numChannels);
    public native void setBandGain(int bandIndex, float gain);
    public native void setReverbLevel(float level);

    private ActivityMainBinding binding;
    private MyNativeProcessor my = new MyNativeProcessor();
    private SonicAudioProcessor sonicA = new SonicAudioProcessor();

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        Uri audioUri = Uri.fromFile(new File(getExternalFilesDir(null), "SoundHelix-Song-1.mp3"));
        Log.i(this.getClass().getName(), String.valueOf(audioUri));
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play(); // 自动播放
        Log.i("Main", "Running start ...");
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