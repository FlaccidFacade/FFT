#ifndef FFT_H
#define FFT_H

#include <vector>
#include <complex>

/**
 * Compute the Fast Fourier Transform of the input signal.
 * Uses the Cooley-Tukey FFT algorithm.
 * 
 * @param x Input signal as a vector of complex numbers
 * @return FFT of the input signal
 */
std::vector<std::complex<double>> fft(const std::vector<std::complex<double>>& x);

#endif // FFT_H
