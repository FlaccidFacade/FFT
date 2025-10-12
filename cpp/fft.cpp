#include "fft.h"
#include <iostream>

int main() {
    // Example usage
    std::vector<double> signal = {1, 1, 1, 1, 0, 0, 0, 0};
    std::vector<std::complex<double>> input;
    for (double val : signal) {
        input.push_back(std::complex<double>(val, 0));
    }
    
    auto result = fft(input);
    
    std::cout << "Input signal: ";
    for (double val : signal) {
        std::cout << val << " ";
    }
    std::cout << std::endl;
    
    std::cout << "FFT result:" << std::endl;
    for (size_t i = 0; i < result.size(); i++) {
        std::cout << "  " << i << ": " 
                  << result[i].real() << " + " 
                  << result[i].imag() << "i" << std::endl;
    }
    
    return 0;
}
