# Python FFT Implementation

This implementation uses only Python's standard library (cmath for complex numbers).

## Building and Running

```bash
# Run the FFT example
python3 fft.py

# Run tests
python3 test_fft.py
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `cmath` for complex number operations
- Automatically pads input to next power of 2
