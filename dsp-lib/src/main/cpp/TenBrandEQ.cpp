#include "FunctionResult.h"
#include <JuceHeader.h>

// EQ Band 参数结构
struct EQBand {
    enum Type { LowShelf, Peak, HighShelf, Bypass } type;
    float frequency = 1000.0f;  // 中心频率
    float gainDb   = 0.0f;      // 增益 (dB)
    float quality  = 1.0f;      // Q 值
};

// 10 段 EQ 处理器
class TenBandEQ {
public:
    TenBandEQ() {
        // 初始化为 Peak 滤波器
        for (int i = 0; i < bands.size(); ++i) {
            bands[i].type = EQBand::Peak;
            bands[i].frequency = bandFrequencies[i];
            bands[i].gainDb = 0.0f;
            bands[i].quality = 1.0f;
        }
    }

    // 准备 (采样率 / 块大小)
    void prepare(const juce::dsp::ProcessSpec& spec) {
        filter.prepare(spec);
        updateFilters(spec.sampleRate);
    }

    // 处理音频
    void process(juce::dsp::AudioBlock<float>& block) {
        juce::dsp::ProcessContextReplacing<float> context(block);
        filter.process(context);
    }

    /**
     * 为频率设置新的增益
     * @param newBand           [in] 新的增益
     * @param frequencyIndex    [in] 频率对应的索引
     * @param sampleRate        [in] 采样率，应该和其它的滤波器系数保持相同的采样率
     */
    FUNCTION_RESULT setBandParameters(const EQBand& newBand, int frequencyIndex, double sampleRate) {
        if (frequencyIndex >= 0 && frequencyIndex < bands.size()) {
            bands[frequencyIndex] = newBand;
            updateFilter(frequencyIndex, sampleRate);
            return FUNCTION_SUCCESS;
        }
        return FUNCTION_FAILED;
    }

    /**
     * 根据频率获取增益。
     * @param gain [out] 增益值，如果找到对应的频带，则设置为该频带的增益；否则设置为0.0f。
     * @param band [in] 要查询的频率
     */
    FUNCTION_RESULT getBandFrequency(float& gain, int frequencyIndex){
        if (frequencyIndex >= 0 && frequencyIndex < bands.size()) {
            gain = bands[frequencyIndex].gainDb;
            return FUNCTION_SUCCESS;
        }

        return FUNCTION_FAILED;
    }

private:
    // 固定的 10 个频率点 (类似图形 EQ)
    const std::array<float, 10> bandFrequencies
            { 31.0f, 62.0f, 125.0f, 250.0f, 500.0f,
              1000.0f, 2000.0f, 4000.0f, 8000.0f, 16000.0f };

    std::array<EQBand, 10> bands;

    // ProcessorChain 里放 10 个滤波器
    using FilterBand = juce::dsp::ProcessorDuplicator<juce::dsp::IIR::Filter<float>, juce::dsp::IIR::Coefficients<float>>;
    juce::dsp::ProcessorChain<
            FilterBand, FilterBand, FilterBand, FilterBand, FilterBand,
            FilterBand, FilterBand, FilterBand, FilterBand, FilterBand> filter;

    // 更新所有滤波器
    void updateFilters(double sampleRate) {
        for (int i = 0; i < bands.size(); ++i)
            updateFilter(i, sampleRate);
    }

    // 更新单个滤波器
    void updateFilter(int index, double sampleRate) {
        auto& band = bands[index];
//        auto& filter = processorChain.get<static_cast<size_t>(index)>();

        using Coefficients = juce::dsp::IIR::Coefficients<float>;
        typename Coefficients::Ptr newCoefficients;
        switch (bands [index].type) {
            case EQBand::LowShelf:
                newCoefficients = Coefficients::makeLowShelf(sampleRate, band.frequency, band.quality,
                                                   juce::Decibels::decibelsToGain(band.gainDb));
                break;
            case EQBand::Peak:
                newCoefficients = Coefficients::makePeakFilter(sampleRate, band.frequency, band.quality,
                                                     juce::Decibels::decibelsToGain(band.gainDb));
                break;
            case EQBand::HighShelf:
                newCoefficients = Coefficients::makeHighShelf(sampleRate, band.frequency, band.quality,
                                                    juce::Decibels::decibelsToGain(band.gainDb));
                break;
            case EQBand::Bypass:
            default:
                newCoefficients = new Coefficients(1.0f, 0.0f, 0.0f, 1.0f); // 直通
                break;
        }

        if (newCoefficients) {
            {
                // minimise lock scope, get<0>() needs to be a  compile time constant
//                juce::ScopedLock processLock (getCallbackLock());
                if (index == 0)
                    *filter.get<0>().state = *newCoefficients;
                else if (index == 1)
                    *filter.get<1>().state = *newCoefficients;
                else if (index == 2)
                    *filter.get<2>().state = *newCoefficients;
                else if (index == 3)
                    *filter.get<3>().state = *newCoefficients;
                else if (index == 4)
                    *filter.get<4>().state = *newCoefficients;
                else if (index == 5)
                    *filter.get<5>().state = *newCoefficients;
                else if (index == 6)
                    *filter.get<6>().state = *newCoefficients;
                else if (index == 7)
                    *filter.get<7>().state = *newCoefficients;
                else if (index == 8)
                    *filter.get<8>().state = *newCoefficients;
                else if (index == 9)
                    *filter.get<9>().state = *newCoefficients;
            }
//            newCoefficients->getMagnitudeForFrequencyArray (frequencies.data(),
//                                                            bands [index].magnitudes.data(),
//                                                            frequencies.size(), sampleRate);

        }
    }
};
