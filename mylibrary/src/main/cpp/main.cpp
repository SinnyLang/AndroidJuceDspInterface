// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("main");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("main")
//      }
//    }

#include <JuceHeader.h>
int main(){
    if (ProjectInfo::projectName == "AndroidJuceDspInterface"){}
    auto* pFft = new juce::dsp::FFT(1);
    pFft->getSize();
    return 0;
}