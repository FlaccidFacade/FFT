#!/usr/bin/env python3
"""
Test suite for FFT implementation.
Tests include correctness, edge cases, and performance benchmarking.
"""
import time
import math
import sys
from fft import fft


def test_correctness():
    """Test 1: Verify FFT correctness with known values."""
    print("\n=== Test 1: Correctness ===")
    
    # Test case 1: Simple signal
    signal = [1, 1, 1, 1, 0, 0, 0, 0]
    result = fft([complex(x) for x in signal])
    
    # DC component should be 4 (sum of all ones)
    dc = result[0]
    assert abs(dc.real - 4.0) < 1e-10, f"DC component should be 4, got {dc.real}"
    
    print(f"✓ DC component test passed: {dc.real:.4f}")
    
    # Test case 2: Impulse (should have flat spectrum)
    impulse = [1, 0, 0, 0, 0, 0, 0, 0]
    result = fft([complex(x) for x in impulse])
    
    # All bins should be 1
    all_ones = all(abs(val.real - 1.0) < 1e-10 and abs(val.imag) < 1e-10 for val in result)
    assert all_ones, "Impulse FFT should have all bins equal to 1"
    
    print("✓ Impulse test passed")
    
    # Test case 3: Constant signal
    constant = [1] * 8
    result = fft([complex(x) for x in constant])
    
    # Only DC component should be non-zero
    assert abs(result[0].real - 8.0) < 1e-10, "DC should be 8"
    for i in range(1, len(result)):
        assert abs(result[i]) < 1e-10, f"Bin {i} should be zero, got {result[i]}"
    
    print("✓ Constant signal test passed")
    print("Test 1: PASSED\n")
    return True


def test_edge_cases():
    """Test 2: Test edge cases."""
    print("=== Test 2: Edge Cases ===")
    
    # Empty input
    result = fft([])
    assert result == [], "Empty input should return empty output"
    print("✓ Empty input test passed")
    
    # Single element
    result = fft([complex(5)])
    assert len(result) == 1 and result[0] == complex(5), "Single element should return itself"
    print("✓ Single element test passed")
    
    # Power of 2 length
    signal = [1, 0, 1, 0]
    result = fft([complex(x) for x in signal])
    assert len(result) == 4, "Length should be preserved for power of 2"
    print("✓ Power of 2 test passed")
    
    # Non-power of 2 (should be padded)
    signal = [1, 2, 3]
    result = fft([complex(x) for x in signal])
    assert len(result) == 4, "Non-power of 2 should be padded to next power"
    print("✓ Non-power of 2 padding test passed")
    
    print("Test 2: PASSED\n")
    return True


def test_performance():
    """Test 3: Performance benchmark."""
    print("=== Test 3: Performance Benchmark ===")
    
    sizes = [64, 256, 1024, 4096]
    
    for size in sizes:
        signal = [complex(i % 2) for i in range(size)]
        
        start = time.perf_counter()
        result = fft(signal)
        end = time.perf_counter()
        
        elapsed_ms = (end - start) * 1000
        print(f"Size {size:5d}: {elapsed_ms:8.4f} ms")
    
    # Performance test - ensure reasonable execution time for 4096 samples
    signal = [complex(i % 2) for i in range(4096)]
    start = time.perf_counter()
    result = fft(signal)
    end = time.perf_counter()
    elapsed = end - start
    
    # Should complete in reasonable time (less than 5 seconds for 4096 samples)
    assert elapsed < 5.0, f"FFT of 4096 samples took too long: {elapsed:.4f}s"
    
    print(f"\n✓ Performance test passed (4096 samples: {elapsed*1000:.4f} ms)")
    print("Test 3: PASSED\n")
    return True


def main():
    """Run all tests."""
    print("=" * 50)
    print("Python FFT Test Suite")
    print("=" * 50)
    
    try:
        test_correctness()
        test_edge_cases()
        test_performance()
        
        print("=" * 50)
        print("ALL TESTS PASSED")
        print("=" * 50)
        return 0
    except AssertionError as e:
        print(f"\n❌ Test failed: {e}")
        return 1
    except Exception as e:
        print(f"\n❌ Unexpected error: {e}")
        return 1


if __name__ == "__main__":
    sys.exit(main())
