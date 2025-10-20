package com.flaccidfacade.fftvisualizer.utils

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Utility functions for FFT analysis
 */
object FFTUtils {
    
    /**
     * Find the dominant frequency from FFT magnitudes
     * @param magnitudes FFT magnitude array
     * @param sampleRate Sample rate in Hz
     * @return Detected frequency in Hz
     */
    fun findDominantFrequency(magnitudes: DoubleArray, sampleRate: Int): Double {
        if (magnitudes.isEmpty()) return 0.0
        
        // Find the index of the maximum magnitude (skip DC component at index 0)
        var maxIndex = 1
        var maxMagnitude = magnitudes[1]
        
        for (i in 2 until magnitudes.size / 2) { // Only check first half (Nyquist)
            if (magnitudes[i] > maxMagnitude) {
                maxMagnitude = magnitudes[i]
                maxIndex = i
            }
        }
        
        // Convert bin index to frequency
        val binWidth = sampleRate.toDouble() / magnitudes.size
        return maxIndex * binWidth
    }
    
    /**
     * Calculate RMS (Root Mean Square) of signal
     */
    fun calculateRMS(signal: DoubleArray): Double {
        if (signal.isEmpty()) return 0.0
        val sumSquares = signal.map { it * it }.sum()
        return sqrt(sumSquares / signal.size)
    }
    
    /**
     * Calculate divergence between two FFT results
     * Uses Mean Squared Error (MSE)
     */
    fun calculateDivergence(fft1: DoubleArray, fft2: DoubleArray): Double {
        if (fft1.size != fft2.size) return Double.MAX_VALUE
        
        var sumSquaredDiff = 0.0
        for (i in fft1.indices) {
            val diff = fft1[i] - fft2[i]
            sumSquaredDiff += diff * diff
        }
        
        return sqrt(sumSquaredDiff / fft1.size)
    }
    
    /**
     * Compute magnitudes from complex FFT result
     */
    fun computeMagnitudes(complexFFT: DoubleArray): DoubleArray {
        val magnitudes = DoubleArray(complexFFT.size / 2)
        for (i in magnitudes.indices) {
            val real = complexFFT[i * 2]
            val imag = complexFFT[i * 2 + 1]
            magnitudes[i] = sqrt(real * real + imag * imag)
        }
        return magnitudes
    }
    
    /**
     * Apply Hamming window to reduce spectral leakage
     */
    fun applyHammingWindow(signal: DoubleArray): DoubleArray {
        val windowed = DoubleArray(signal.size)
        val n = signal.size
        for (i in signal.indices) {
            val window = 0.54 - 0.46 * kotlin.math.cos(2.0 * Math.PI * i / (n - 1))
            windowed[i] = signal[i] * window
        }
        return windowed
    }
}
