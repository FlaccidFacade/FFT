package com.flaccidfacade.fftvisualizer.fft

import org.junit.Test
import org.junit.Assert.*
import kotlin.math.abs

/**
 * Unit tests for JavaFFT implementation
 */
class JavaFFTTest {
    
    private val fft = JavaFFT()
    private val epsilon = 1e-6
    
    @Test
    fun testDCComponent() {
        // Test with constant signal (DC component)
        val signal = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
        val result = fft.fftFromDoubles(signal)
        
        // First element should be sum of inputs
        assertEquals(4.0, result[0].real, epsilon)
        assertEquals(0.0, result[0].imag, epsilon)
        
        // All other components should be near zero
        for (i in 1 until result.size) {
            assertTrue(result[i].abs() < epsilon)
        }
    }
    
    @Test
    fun testImpulse() {
        // Test with impulse signal [1, 0, 0, 0]
        val signal = doubleArrayOf(1.0, 0.0, 0.0, 0.0)
        val result = fft.fftFromDoubles(signal)
        
        // All frequency bins should have magnitude 1
        for (i in result.indices) {
            assertEquals(1.0, result[i].abs(), epsilon)
        }
    }
    
    @Test
    fun testSineWave() {
        // Test with a simple sine wave
        val n = 8
        val signal = DoubleArray(n) { i ->
            kotlin.math.sin(2.0 * Math.PI * i / n)
        }
        
        val result = fft.fftFromDoubles(signal)
        
        // The FFT of a sine wave should have peaks at specific frequencies
        assertNotNull(result)
        assertEquals(n, result.size)
        
        // Sum of all magnitudes should be non-zero
        val totalMagnitude = result.sumOf { it.abs() }
        assertTrue(totalMagnitude > 0.0)
    }
    
    @Test
    fun testPowerOfTwo() {
        // Test that power-of-2 sizes work
        val sizes = listOf(2, 4, 8, 16, 32, 64, 128, 256)
        
        for (size in sizes) {
            val signal = DoubleArray(size) { 1.0 }
            val result = fft.fftFromDoubles(signal)
            
            // Should return array of same size
            assertEquals(size, result.size)
            
            // DC component should equal sum
            assertEquals(size.toDouble(), result[0].real, epsilon)
        }
    }
    
    @Test
    fun testNonPowerOfTwo() {
        // Test that non-power-of-2 sizes are padded correctly
        val signal = doubleArrayOf(1.0, 1.0, 1.0)  // Size 3
        val result = fft.fftFromDoubles(signal)
        
        // Should be padded to size 4
        assertEquals(4, result.size)
        
        // DC component should equal sum of original signal
        assertEquals(3.0, result[0].real, epsilon)
    }
    
    @Test
    fun testEmpty() {
        // Test with empty input
        val signal = doubleArrayOf()
        val result = fft.fftFromDoubles(signal)
        
        assertEquals(0, result.size)
    }
    
    @Test
    fun testSingleElement() {
        // Test with single element
        val signal = doubleArrayOf(5.0)
        val result = fft.fftFromDoubles(signal)
        
        assertEquals(1, result.size)
        assertEquals(5.0, result[0].real, epsilon)
        assertEquals(0.0, result[0].imag, epsilon)
    }
    
    @Test
    fun testComplexOperations() {
        // Test Complex class operations
        val c1 = JavaFFT.Complex(3.0, 4.0)
        val c2 = JavaFFT.Complex(1.0, 2.0)
        
        // Addition
        val sum = c1 + c2
        assertEquals(4.0, sum.real, epsilon)
        assertEquals(6.0, sum.imag, epsilon)
        
        // Subtraction
        val diff = c1 - c2
        assertEquals(2.0, diff.real, epsilon)
        assertEquals(2.0, diff.imag, epsilon)
        
        // Multiplication
        val prod = c1 * c2
        assertEquals(-5.0, prod.real, epsilon)  // 3*1 - 4*2 = -5
        assertEquals(10.0, prod.imag, epsilon)  // 3*2 + 4*1 = 10
        
        // Magnitude
        assertEquals(5.0, c1.abs(), epsilon)  // sqrt(3^2 + 4^2) = 5
    }
}
