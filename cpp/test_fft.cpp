#include "fft.h"
#include <iostream>
#include <chrono>
#include <cmath>
#include <cassert>
#include <iomanip>

bool test_correctness() {
    std::cout << "\n=== Test 1: Correctness ===" << std::endl;
    
    // Test case 1: Simple signal
    std::vector<std::complex<double>> signal;
    for (int i = 0; i < 8; i++) {
        signal.push_back(std::complex<double>(i < 4 ? 1 : 0, 0));
    }
    
    auto result = fft(signal);
    
    // DC component should be 4
    double dc = result[0].real();
    assert(std::abs(dc - 4.0) < 1e-10);
    std::cout << "✓ DC component test passed: " << std::fixed << std::setprecision(4) << dc << std::endl;
    
    // Test case 2: Impulse
    std::vector<std::complex<double>> impulse(8, std::complex<double>(0, 0));
    impulse[0] = std::complex<double>(1, 0);
    
    result = fft(impulse);
    
    // All bins should be 1
    bool all_ones = true;
    for (const auto& val : result) {
        if (std::abs(val.real() - 1.0) >= 1e-10 || std::abs(val.imag()) >= 1e-10) {
            all_ones = false;
            break;
        }
    }
    assert(all_ones);
    std::cout << "✓ Impulse test passed" << std::endl;
    
    // Test case 3: Constant signal
    std::vector<std::complex<double>> constant(8, std::complex<double>(1, 0));
    result = fft(constant);
    
    // Only DC component should be non-zero
    assert(std::abs(result[0].real() - 8.0) < 1e-10);
    for (size_t i = 1; i < result.size(); i++) {
        assert(std::abs(result[i]) < 1e-10);
    }
    std::cout << "✓ Constant signal test passed" << std::endl;
    std::cout << "Test 1: PASSED\n" << std::endl;
    
    return true;
}

bool test_edge_cases() {
    std::cout << "=== Test 2: Edge Cases ===" << std::endl;
    
    // Empty input
    std::vector<std::complex<double>> empty;
    auto result = fft(empty);
    assert(result.empty());
    std::cout << "✓ Empty input test passed" << std::endl;
    
    // Single element
    std::vector<std::complex<double>> single = {std::complex<double>(5, 0)};
    result = fft(single);
    assert(result.size() == 1 && result[0] == std::complex<double>(5, 0));
    std::cout << "✓ Single element test passed" << std::endl;
    
    // Power of 2 length
    std::vector<std::complex<double>> power_of_2 = {
        std::complex<double>(1, 0),
        std::complex<double>(0, 0),
        std::complex<double>(1, 0),
        std::complex<double>(0, 0)
    };
    result = fft(power_of_2);
    assert(result.size() == 4);
    std::cout << "✓ Power of 2 test passed" << std::endl;
    
    // Non-power of 2
    std::vector<std::complex<double>> non_power = {
        std::complex<double>(1, 0),
        std::complex<double>(2, 0),
        std::complex<double>(3, 0)
    };
    result = fft(non_power);
    assert(result.size() == 4);
    std::cout << "✓ Non-power of 2 padding test passed" << std::endl;
    
    std::cout << "Test 2: PASSED\n" << std::endl;
    return true;
}

bool test_performance() {
    std::cout << "=== Test 3: Performance Benchmark ===" << std::endl;
    
    int sizes[] = {64, 256, 1024, 4096};
    
    for (int size : sizes) {
        std::vector<std::complex<double>> signal;
        for (int i = 0; i < size; i++) {
            signal.push_back(std::complex<double>(i % 2, 0));
        }
        
        auto start = std::chrono::high_resolution_clock::now();
        auto result = fft(signal);
        auto end = std::chrono::high_resolution_clock::now();
        
        auto elapsed = std::chrono::duration_cast<std::chrono::microseconds>(end - start);
        std::cout << "Size " << std::setw(5) << size << ": " 
                  << std::setw(8) << std::fixed << std::setprecision(4) 
                  << elapsed.count() / 1000.0 << " ms" << std::endl;
    }
    
    // Performance test
    std::vector<std::complex<double>> signal;
    for (int i = 0; i < 4096; i++) {
        signal.push_back(std::complex<double>(i % 2, 0));
    }
    
    auto start = std::chrono::high_resolution_clock::now();
    auto result = fft(signal);
    auto end = std::chrono::high_resolution_clock::now();
    
    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    assert(elapsed.count() < 5000);
    
    std::cout << "\n✓ Performance test passed (4096 samples: " 
              << elapsed.count() << " ms)" << std::endl;
    std::cout << "Test 3: PASSED\n" << std::endl;
    
    return true;
}

int main() {
    std::cout << "==================================================" << std::endl;
    std::cout << "C++ FFT Test Suite" << std::endl;
    std::cout << "==================================================" << std::endl;
    
    try {
        test_correctness();
        test_edge_cases();
        test_performance();
        
        std::cout << "==================================================" << std::endl;
        std::cout << "ALL TESTS PASSED" << std::endl;
        std::cout << "==================================================" << std::endl;
        return 0;
    } catch (const std::exception& e) {
        std::cerr << "\n❌ Test failed: " << e.what() << std::endl;
        return 1;
    }
}
