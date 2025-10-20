package com.flaccidfacade.fftvisualizer.fft

/**
 * Java-based FFT implementation (Cooley-Tukey algorithm)
 * Direct port from the repository's Java implementation
 */
class JavaFFT {
    
    data class Complex(val real: Double, val imag: Double) {
        operator fun plus(other: Complex) = Complex(real + other.real, imag + other.imag)
        operator fun minus(other: Complex) = Complex(real - other.real, imag - other.imag)
        operator fun times(other: Complex) = Complex(
            real * other.real - imag * other.imag,
            real * other.imag + imag * other.real
        )
        
        fun abs() = kotlin.math.sqrt(real * real + imag * imag)
    }
    
    fun fft(x: Array<Complex>): Array<Complex> {
        var n = x.size
        
        // Base case
        if (n <= 1) return x
        
        // Pad to next power of 2 if necessary
        if ((n and (n - 1)) != 0) {
            var nextPow2 = 1
            while (nextPow2 < n) {
                nextPow2 = nextPow2 shl 1
            }
            val padded = Array(nextPow2) { i ->
                if (i < n) x[i] else Complex(0.0, 0.0)
            }
            return fft(padded)
        }
        
        // Divide
        val even = Array(n / 2) { i -> x[2 * i] }
        val odd = Array(n / 2) { i -> x[2 * i + 1] }
        
        // Conquer
        val evenFFT = fft(even)
        val oddFFT = fft(odd)
        
        // Combine
        val result = Array(n) { Complex(0.0, 0.0) }
        for (k in 0 until n / 2) {
            val angle = -2.0 * Math.PI * k / n
            val t = Complex(Math.cos(angle), Math.sin(angle)) * oddFFT[k]
            result[k] = evenFFT[k] + t
            result[k + n / 2] = evenFFT[k] - t
        }
        
        return result
    }
    
    fun fftFromDoubles(signal: DoubleArray): Array<Complex> {
        val input = Array(signal.size) { i -> Complex(signal[i], 0.0) }
        return fft(input)
    }
}
