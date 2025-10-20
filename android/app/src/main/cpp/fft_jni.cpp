#include <jni.h>
#include <string>
#include <vector>
#include <complex>
#include <android/log.h>
#include "fft.h"

#define LOG_TAG "FFT_Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jdoubleArray JNICALL
Java_com_flaccidfacade_fftvisualizer_fft_NativeFFT_computeFFT(
    JNIEnv* env,
    jobject /* this */,
    jdoubleArray input
) {
    // Get input array
    jsize length = env->GetArrayLength(input);
    jdouble* inputData = env->GetDoubleArrayElements(input, nullptr);
    
    if (inputData == nullptr) {
        LOGE("Failed to get input array");
        return nullptr;
    }
    
    // Convert to complex numbers
    std::vector<std::complex<double>> complexInput(length);
    for (int i = 0; i < length; i++) {
        complexInput[i] = std::complex<double>(inputData[i], 0.0);
    }
    
    // Release input array
    env->ReleaseDoubleArrayElements(input, inputData, JNI_ABORT);
    
    // Compute FFT
    std::vector<std::complex<double>> result;
    try {
        result = fft(complexInput);
    } catch (const std::exception& e) {
        LOGE("FFT computation failed: %s", e.what());
        return nullptr;
    }
    
    // Convert result to interleaved real/imaginary array
    jdoubleArray output = env->NewDoubleArray(result.size() * 2);
    if (output == nullptr) {
        LOGE("Failed to allocate output array");
        return nullptr;
    }
    
    std::vector<double> outputData(result.size() * 2);
    for (size_t i = 0; i < result.size(); i++) {
        outputData[i * 2] = result[i].real();
        outputData[i * 2 + 1] = result[i].imag();
    }
    
    env->SetDoubleArrayRegion(output, 0, result.size() * 2, outputData.data());
    
    return output;
}

} // extern "C"
