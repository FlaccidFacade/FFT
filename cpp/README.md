# C++ FFT Implementation

This implementation uses only C++ standard library (complex numbers from `<complex>`).

## Building and Running

```bash
# Build
make

# Run the FFT example
./fft

# Run tests
./test_fft

# Clean
make clean
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `<complex>`, `<vector>`, `<cmath>`
- Automatically pads input to next power of 2
- C++11 or later required
