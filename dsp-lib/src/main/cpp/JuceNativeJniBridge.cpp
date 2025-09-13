#include <jni.h>

//
// Created by SinnyLang on 2025/9/7.
//
#include <JuceHeader.h>
#include "TenBrandEQ.cpp"

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
                                                    jint length) {
    // TODO: implement processPCM()
    // 设置某个频段
//    EQBand band;
//    band.type = EQBand::Peak;
//    band.frequency = 1000.0f;
//    band.gainDb = 6.0f;   // 提升 6 dB
//    band.quality = 0.7f;
//    eq.setBandParameters(5, band, spec.sampleRate); // 修改第 6 个 band


    // length = interleaved PCM 总长度（包含两个声道）
    // 样本数 perChannel = length / 2
    const int numChannels = 2;
    const int numSamplesPerChannel = length / numChannels;

    // 获取 Java 层 PCM
    jshort* inputData = env->GetShortArrayElements(data, nullptr);

    // 创建 JUCE 缓冲区 (2 通道)
    juce::AudioBuffer<float> buffer(numChannels, numSamplesPerChannel);

    // -------- 拆分 interleaved short[] -> float planar --------
    for (int i = 0; i < numSamplesPerChannel; ++i)
    {
        buffer.setSample(0, i, inputData[2 * i]     / 32768.0f); // 左声道
        buffer.setSample(1, i, inputData[2 * i + 1] / 32768.0f); // 右声道
    }

    // 在 processBlock 里处理
    juce::dsp::AudioBlock<float> block(buffer);
    eq.process(block);

    // -------- 合并 float planar -> interleaved short[] --------
    for (int i = 0; i < numSamplesPerChannel; ++i)
    {
        inputData[2 * i]     =
                (jshort) juce::jlimit(-32768, 32767,
                                      (int)(buffer.getSample(0, i) * 32768.0f));
        inputData[2 * i + 1] =
                (jshort) juce::jlimit(-32768, 32767,
                                      (int)(buffer.getSample(1, i) * 32768.0f));
    }

    // -------- 写回 Java --------
    env->ReleaseShortArrayElements(data, inputData, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_xyz_sl_dsp_juce_JuceNativeInterface_prepare(JNIEnv *env, jobject thiz,
                                                 jint sample_rate,
                                                 jint length) {
    // 初始化
    juce::dsp::ProcessSpec spec{};
    spec.sampleRate = (double) sample_rate;
    spec.maximumBlockSize = (juce::uint32) length / 2 ;
    spec.numChannels = (juce::uint32) 2;
    eq.prepare(spec);

    EQBand band;
    band.type = EQBand::Peak;
    band.frequency = 62.0f;
    band.gainDb = 0.0f;   // 提升 6 dB
    band.quality = 1.0f;
    eq.setBandParameters(5, band, spec.sampleRate); // 修改第 6 个 band
}