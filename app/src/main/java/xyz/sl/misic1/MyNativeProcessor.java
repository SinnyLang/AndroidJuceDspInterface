package xyz.sl.misic1;


import androidx.media3.common.C;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.BaseAudioProcessor;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

@UnstableApi
public class MyNativeProcessor extends BaseAudioProcessor {
    public static native void processPCM(short[] data, int length, int sampleRate);

    @Override
    public void queueInput(ByteBuffer inputBuffer) {
        Log.i("MyAduio", "");
        Log.i("MyAudio", "queueInput inputBuffer1="+inputBuffer);
        int len = inputBuffer.remaining();
        ByteBuffer buffer = replaceOutputBuffer(len);

        short[] pcm = new short[len / 2];
        for (int i = 0; i < pcm.length; i++) {
            pcm[i] = inputBuffer.getShort();
        }

        processPCM(pcm, pcm.length, inputAudioFormat.sampleRate);
        Log.i("MyAudio", "queueInput pcm(processed)=" + Arrays.toString(pcm));

        // 写回到 outputBuffer
        for (short s : pcm) {
            buffer.putShort(s);
        }
        buffer.flip(); // !!! 必须 flip
        Log.i("MyAudio", "queueInput inputBuffer2="+inputBuffer);
    }

    @Override
    public ByteBuffer getOutput() {
        ByteBuffer output = super.getOutput();
        Log.i("MyAudio", "getOutput outputBuffer="+output);
        return output;
    }

    @Override
    protected AudioFormat onConfigure(AudioFormat inputAudioFormat) throws UnhandledAudioFormatException {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
            throw new UnhandledAudioFormatException(inputAudioFormat);
        }

        return new AudioFormat(
                inputAudioFormat.sampleRate,
                inputAudioFormat.channelCount,
                inputAudioFormat.encoding
        );
    }
}

