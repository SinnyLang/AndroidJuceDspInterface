package xyz.sl.dsp.juce;

public class JuceNativeInterface {
    static {
        System.loadLibrary("JuceDsp");
    }

    public native String getVersion();

    public native void processPCM(short[] data, int length);

    public native void prepare(int sampleRate, int length);
}
