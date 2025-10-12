#!/usr/bin/env node
/**
 * FFT implementation using JavaScript standard library.
 * Implements the Cooley-Tukey FFT algorithm.
 */

class Complex {
    constructor(real, imag) {
        this.real = real;
        this.imag = imag;
    }
    
    add(other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }
    
    subtract(other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }
    
    multiply(other) {
        const newReal = this.real * other.real - this.imag * other.imag;
        const newImag = this.real * other.imag + this.imag * other.real;
        return new Complex(newReal, newImag);
    }
    
    abs() {
        return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }
    
    toString() {
        return `${this.real.toFixed(4)} + ${this.imag.toFixed(4)}i`;
    }
}

/**
 * Compute the Fast Fourier Transform of the input signal.
 * 
 * @param {Complex[]} x - Input signal as an array of complex numbers
 * @returns {Complex[]} FFT of the input signal
 */
function fft(x) {
    let n = x.length;
    
    // Base case
    if (n <= 1) {
        return x;
    }
    
    // Pad to next power of 2 if necessary
    if ((n & (n - 1)) !== 0) {
        let nextPow2 = 1;
        while (nextPow2 < n) {
            nextPow2 <<= 1;
        }
        const padded = [...x];
        while (padded.length < nextPow2) {
            padded.push(new Complex(0, 0));
        }
        x = padded;
        n = nextPow2;
    }
    
    // Divide
    const even = [];
    const odd = [];
    for (let i = 0; i < n; i += 2) {
        even.push(x[i]);
    }
    for (let i = 1; i < n; i += 2) {
        odd.push(x[i]);
    }
    
    // Conquer
    const evenFFT = fft(even);
    const oddFFT = fft(odd);
    
    // Combine
    const result = new Array(n);
    for (let k = 0; k < n / 2; k++) {
        const angle = -2 * Math.PI * k / n;
        const t = new Complex(Math.cos(angle), Math.sin(angle)).multiply(oddFFT[k]);
        result[k] = evenFFT[k].add(t);
        result[k + n / 2] = evenFFT[k].subtract(t);
    }
    
    return result;
}

// Main function for demonstration
function main() {
    const signal = [1, 1, 1, 1, 0, 0, 0, 0];
    const input = signal.map(x => new Complex(x, 0));
    
    const result = fft(input);
    
    console.log('Input signal:', signal);
    console.log('FFT result:');
    result.forEach((val, i) => {
        console.log(`  ${i}: ${val}`);
    });
}

// Run main if executed directly
if (require.main === module) {
    main();
}

// Export for testing
module.exports = { Complex, fft };
