package xyz.sl.dsp.juce;

/**
 * 均衡器频率波段枚举
 * <p>定义了10段均衡器的标准中心频率及其在 C++ 代码中对应的索引值</p>
 */
public enum EqualizerFrequency {
    FREQUENCY_31(31f, 0), FREQUENCY_62(62f, 1),
    FREQUENCY_125(125f, 2), FREQUENCY_250(250f, 3),
    FREQUENCY_500(500f, 4), FREQUENCY_1k(1000f, 5),
    FREQUENCY_2k(2000f, 6), FREQUENCY_4k(4000f, 7),
    FREQUENCY_8k(8000f, 8), FREQUENCY_16k(16000f, 9);

    public final float frequency;
    public final int index;
    EqualizerFrequency(float frequency, int index) {
        this.frequency = frequency;
        this.index = index;
    }
}
