package com.flaccidfacade.fftvisualizer.fft

/**
 * Native C++ FFT implementation wrapper
 * Uses JNI to call the C++ FFT from the repository
 */
class NativeFFT {
    
    companion object {
        init {
            System.loadLibrary("fft_native")
        }
    }
    
    /**
     * Compute FFT using native C++ implementation
     * @param input Array of real values
     * @return Array of complex numbers [real0, imag0, real1, imag1, ...]
     */
    external fun computeFFT(input: DoubleArray): DoubleArray
    
    /**
     * Convenience method that returns magnitudes
     */
    fun computeFFTMagnitudes(input: DoubleArray): DoubleArray {
        val result = computeFFT(input)
        val magnitudes = DoubleArray(result.size / 2)
        for (i in magnitudes.indices) {
            val real = result[i * 2]
            val imag = result[i * 2 + 1]
            magnitudes[i] = kotlin.math.sqrt(real * real + imag * imag)
        }
        return magnitudes
    }
}
