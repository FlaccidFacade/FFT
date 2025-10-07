/**
 * FFT implementation using TypeScript standard library.
 * Implements the Cooley-Tukey FFT algorithm.
 */
export declare class Complex {
    readonly real: number;
    readonly imag: number;
    constructor(real: number, imag: number);
    add(other: Complex): Complex;
    subtract(other: Complex): Complex;
    multiply(other: Complex): Complex;
    abs(): number;
    toString(): string;
}
/**
 * Compute the Fast Fourier Transform of the input signal.
 *
 * @param x - Input signal as an array of complex numbers
 * @returns FFT of the input signal
 */
export declare function fft(x: Complex[]): Complex[];
