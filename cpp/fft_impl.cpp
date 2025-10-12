#include "fft.h"
#include <cmath>

std::vector<std::complex<double>> fft(const std::vector<std::complex<double>>& x) {
    size_t n = x.size();
    
    // Base case
    if (n <= 1) {
        return x;
    }
    
    // Make a copy to work with
    std::vector<std::complex<double>> input = x;
    
    // Pad to next power of 2 if necessary
    if ((n & (n - 1)) != 0) {
        size_t next_pow2 = 1;
        while (next_pow2 < n) {
            next_pow2 <<= 1;
        }
        input.resize(next_pow2, 0);
        n = next_pow2;
    }
    
    // Divide
    std::vector<std::complex<double>> even, odd;
    for (size_t i = 0; i < n; i += 2) {
        even.push_back(input[i]);
    }
    for (size_t i = 1; i < n; i += 2) {
        odd.push_back(input[i]);
    }
    
    // Conquer
    even = fft(even);
    odd = fft(odd);
    
    // Combine
    std::vector<std::complex<double>> result(n);
    for (size_t k = 0; k < n / 2; k++) {
        std::complex<double> t = std::polar(1.0, -2 * M_PI * k / n) * odd[k];
        result[k] = even[k] + t;
        result[k + n / 2] = even[k] - t;
    }
    
    return result;
}
