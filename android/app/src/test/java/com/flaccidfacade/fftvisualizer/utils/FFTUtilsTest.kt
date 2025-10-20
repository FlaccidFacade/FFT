package com.flaccidfacade.fftvisualizer.utils

import org.junit.Test
import org.junit.Assert.*
import kotlin.math.abs

/**
 * Unit tests for FFTUtils
 */
class FFTUtilsTest {
    
    private val epsilon = 1e-6
    
    @Test
    fun testFindDominantFrequency() {
        // Create a magnitude array with a clear peak at bin 5
        val magnitudes = DoubleArray(16) { i ->
            if (i == 5) 10.0 else 1.0
        }
        
        val sampleRate = 44100
        val frequency = FFTUtils.findDominantFrequency(magnitudes, sampleRate)
        
        // Expected frequency = bin_index * (sample_rate / fft_size)
        // = 5 * (44100 / 16) = 5 * 2756.25 = 13781.25 Hz
        val expected = 5.0 * (44100.0 / 16.0)
        assertEquals(expected, frequency, epsilon)
    }
    
    @Test
    fun testCalculateRMS() {
        // Test RMS calculation
        val signal = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val rms = FFTUtils.calculateRMS(signal)
        
        // RMS = sqrt((1^2 + 2^2 + 3^2 + 4^2) / 4) = sqrt(30/4) = sqrt(7.5)
        val expected = kotlin.math.sqrt(7.5)
        assertEquals(expected, rms, epsilon)
    }
    
    @Test
    fun testCalculateRMSEmpty() {
        val signal = doubleArrayOf()
        val rms = FFTUtils.calculateRMS(signal)
        assertEquals(0.0, rms, epsilon)
    }
    
    @Test
    fun testCalculateDivergence() {
        val fft1 = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val fft2 = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        
        // Identical arrays should have zero divergence
        val divergence = FFTUtils.calculateDivergence(fft1, fft2)
        assertEquals(0.0, divergence, epsilon)
    }
    
    @Test
    fun testCalculateDivergenceDifferent() {
        val fft1 = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val fft2 = doubleArrayOf(2.0, 3.0, 4.0, 5.0)
        
        // MSE = sqrt(((1-2)^2 + (2-3)^2 + (3-4)^2 + (4-5)^2) / 4)
        // = sqrt((1 + 1 + 1 + 1) / 4) = sqrt(1) = 1.0
        val divergence = FFTUtils.calculateDivergence(fft1, fft2)
        assertEquals(1.0, divergence, epsilon)
    }
    
    @Test
    fun testComputeMagnitudes() {
        // Complex array: [3+4i, 5+12i]
        // Interleaved: [3, 4, 5, 12]
        val complexFFT = doubleArrayOf(3.0, 4.0, 5.0, 12.0)
        val magnitudes = FFTUtils.computeMagnitudes(complexFFT)
        
        assertEquals(2, magnitudes.size)
        assertEquals(5.0, magnitudes[0], epsilon)   // sqrt(3^2 + 4^2) = 5
        assertEquals(13.0, magnitudes[1], epsilon)  // sqrt(5^2 + 12^2) = 13
    }
    
    @Test
    fun testApplyHammingWindow() {
        val signal = DoubleArray(8) { 1.0 }
        val windowed = FFTUtils.applyHammingWindow(signal)
        
        assertEquals(signal.size, windowed.size)
        
        // Windowed signal should be different from original
        var different = false
        for (i in signal.indices) {
            if (abs(signal[i] - windowed[i]) > epsilon) {
                different = true
                break
            }
        }
        assertTrue(different)
        
        // Check that window is symmetric
        for (i in 0 until signal.size / 2) {
            assertEquals(
                windowed[i],
                windowed[signal.size - 1 - i],
                epsilon
            )
        }
    }
}
