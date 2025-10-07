# Java FFT Implementation

This implementation uses only Java standard library (no external dependencies).

## Building and Running

```bash
# Build
javac FFT.java
javac TestFFT.java

# Or use the build script
chmod +x build.sh
./build.sh

# Run the FFT example
java FFT

# Run tests
java TestFFT
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `Math` class for trigonometric functions
- Custom Complex number class
- Automatically pads input to next power of 2
- Java 8 or later required
