#!/usr/bin/env python3
"""
FFT implementation using Python standard library (cmath for complex numbers).
Implements the Cooley-Tukey FFT algorithm.
"""
import cmath
import math
from typing import List


def fft(x: List[complex]) -> List[complex]:
    """
    Compute the Fast Fourier Transform of the input signal.
    
    Args:
        x: Input signal as a list of complex numbers
        
    Returns:
        FFT of the input signal
    """
    n = len(x)
    
    # Base case
    if n <= 1:
        return x
    
    # Ensure length is power of 2 by padding with zeros
    if n & (n - 1) != 0:
        next_pow2 = 2 ** math.ceil(math.log2(n))
        x = x + [0] * (next_pow2 - n)
        n = next_pow2
    
    # Divide
    even = fft([x[i] for i in range(0, n, 2)])
    odd = fft([x[i] for i in range(1, n, 2)])
    
    # Conquer
    result = [0] * n
    for k in range(n // 2):
        t = cmath.exp(-2j * cmath.pi * k / n) * odd[k]
        result[k] = even[k] + t
        result[k + n // 2] = even[k] - t
    
    return result


def main():
    """Main function to demonstrate FFT usage."""
    # Example usage
    signal = [1, 1, 1, 1, 0, 0, 0, 0]
    result = fft([complex(x) for x in signal])
    
    print("Input signal:", signal)
    print("FFT result:")
    for i, val in enumerate(result):
        print(f"  {i}: {val.real:.4f} + {val.imag:.4f}i")


if __name__ == "__main__":
    main()
