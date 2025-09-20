#include "TenBrandEQ.cpp"
#include <JuceHeader.h>
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_getVersion(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("0.0.1");
}


TenBandEQ eq = TenBandEQ();

extern "C"
JNIEXPORT void JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_processPCM(JNIEnv *env, jobject thiz,
                                                    jshortArray data,
                                                    jint bytesPerFrame,
                                                    jint channels) {
    // TODO: implement processPCM()
    // 设置某个频段

    // length = interleaved PCM 总长度（包含两个声道）
    // 样本数 perChannel = length / 2
    const int numSamplesPerChannel = bytesPerFrame / channels;

    // 获取 Java 层 PCM
    jshort* inputData = env->GetShortArrayElements(data, nullptr);

    // 创建 JUCE 缓冲区 (2 通道)
    juce::AudioBuffer<float> buffer(channels, numSamplesPerChannel);

    // -------- 拆分 interleaved short[] -> float planar --------
    for (int i = 0; i < numSamplesPerChannel; ++i)
    {
        for (int j = 0; j < channels; ++j) {
            buffer.setSample(j, i, inputData[(2 * i) + j] / 32768.0f); // 左声道
//            buffer.setSample(1, i, inputData[2 * i + 1] / 32768.0f); // 右声道
        }
    }

    // 在 processBlock 里处理
    juce::dsp::AudioBlock<float> block(buffer);
    eq.process(block);

    // -------- 合并 float planar -> interleaved short[] --------
    for (int i = 0; i < numSamplesPerChannel; ++i)
    {
        for (int j = 0; j < channels; ++j) {
            inputData[(2 * i) + j] =
                    (jshort) juce::jlimit(-32768, 32767,
                                          (int)(buffer.getSample(j, i) * 32768.0f));
//            inputData[2 * i + 1] =
//                    (jshort) juce::jlimit(-32768, 32767,
//                                          (int)(buffer.getSample(1, i) * 32768.0f));
        }
    }

    // -------- 写回 Java --------
    env->ReleaseShortArrayElements(data, inputData, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_prepare(JNIEnv *env, jobject thiz,
                                                 jint sample_rate,
                                                 jint maximumBlockSize,
                                                 jint numChannels) {
    // 初始化
    juce::dsp::ProcessSpec spec{};
    spec.sampleRate = (double) sample_rate;
    spec.maximumBlockSize = (juce::uint32) maximumBlockSize / numChannels ;
    spec.numChannels = (juce::uint32) numChannels;
    eq.prepare(spec);

//    EQBand band;
//    band.type = EQBand::Peak;
//    band.frequency = 62.0f;
//    band.gainDb = 0.0f;   // 提升 6 dB
//    band.quality = 1.0f;
//    eq.setBandParameters(5, band, spec.sampleRate); // 修改第 6 个 band
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_getEqualizerBrandGain(JNIEnv *env, jobject thiz,
                                                               jfloat frequency,
                                                               jint frequencyIndex) {
    float gain{ 0.0f };
    if (FUNCTION_FAILED == eq.getBandFrequency(gain, frequencyIndex)){
        return 0.0f;
    }
    return gain;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_setEqualizerBrandGain(JNIEnv *env, jobject thiz,
                                                               jfloat frequency,
                                                               jint frequencyIndex,
                                                               jfloat new_gain,
                                                               jint sampleRate) {
    EQBand band;
    band.type = EQBand::Peak;
    band.frequency = frequency;
    band.gainDb = new_gain;
    band.quality = 1.0f;

    if (FUNCTION_FAILED == eq.setBandParameters(band, frequencyIndex, (double)sampleRate)) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}
