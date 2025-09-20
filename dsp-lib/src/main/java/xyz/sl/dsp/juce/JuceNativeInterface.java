package xyz.sl.dsp.juce;

/**<h5>Provides a Java interface to the native JUCE DSP library.</h2>
 *
 * <p>This class uses the Java Native Interface (JNI) to call functions
 *  * implemented in C++ within the "JuceDsp" shared library. It is responsible
 *  * for loading the native library and declaring the native methods that
 *  * can be called from Java.</p>
 *
 * <p>The typical workflow involves:</p>
 * <p>1. Calling {@link #prepare(int, int, int)} to initialize the native processing environment.</p>
 * <p>2. Repeatedly calling {@link #processPCM(short[], int, int)} to process 16 bit audio data.</p>
 * <p>3. Optionally, calling {@link #getVersion()} to retrieve the native library's version.</p>
 *
 * <hr>
 * TODO: Add more useful interfaces.
 */
public class JuceNativeInterface {
    static {
        System.loadLibrary("JuceDsp");
    }

    private static final JuceNativeInterface j = new JuceNativeInterface();

    private JuceNativeInterface(){}

    /**
     * 获取 JuceNativeInterface 的单例实例
     * @return JuceNativeInterface 的共享实例
     */
    public static JuceNativeInterface getJuceNativeInterface() {
        return j;
    }

    public native String getVersion();

    /**
     * <p>处理 16 位 PCM 音频数据，数据以 short 数组形式传入。</p>
     *
     * <p>注意，数据包含若干帧，每个帧的数据由音频声道数的数据排列而成。
     * 例如有两个声道，则实际处理长度为 bytesPerFrame/2 </p>
     * @param data 包含 PCM 数据的 short 数组
     * @param bytesPerFrame 每帧的字节数
     * @param channels 音频通道数
     */
    public native void processPCM(short[] data, int bytesPerFrame, int channels);

    /**
     * 初始化音频处理环境
     * @param sampleRate 采样率 (Hz)
     * @param bytesPerFrame 每帧的字节数
     * @param channels 音频通道数
     */
    public native void prepare(int sampleRate, int bytesPerFrame, int channels);

    /**
     * 获取指定频率波段的增益值
     * @param frequency 频率值 (Hz)
     * @param frequencyIndex 频率波段索引
     * @return 当前增益值 (dB)
     */
    public native float getEqualizerBrandGain(float frequency, int frequencyIndex);

//    public native float getEqualizerSampleRate();

    /**
     * 设置指定频率波段的增益值
     * @param frequency 频率值 (Hz)
     * @param frequencyIndex 频率波段索引
     * @param newGain 新的增益值 (dB)
     * @param sampleRate 采样率 (Hz)
     * @return 设置成功返回 true，失败返回 false
     */
    public native boolean setEqualizerBrandGain(float frequency, int frequencyIndex,float newGain, int sampleRate);
}
