#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("misic1");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("misic1")
//      }
//    }
extern "C"
JNIEXPORT void JNICALL
Java_xyz_sl_misic1_MyNativeProcessor_processPCM(
    JNIEnv *env,
    jclass clazz,
    jfloatArray data,
    jint length,
    jint sample_rate) {
    // TODO: implement processPCM()
    // 获取 short[] 数据指针
    jfloat* pcmData = env->GetFloatArrayElements(data, nullptr);

    for (int i = 0; i < length; i++) {
        // 示例：音量减半
        pcmData[i] = pcmData[i] / 1;

        // 示例：简单失真效果
        // pcmData[i] = std::tanh(pcmData[i] / 32768.0f) * 32767;
    }

    // 写回 Java 数组
    env->ReleaseFloatArrayElements(data, pcmData, 0);
}