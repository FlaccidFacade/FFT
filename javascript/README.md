# JavaScript FFT Implementation

This implementation uses only JavaScript standard library (no external dependencies).

## Building and Running

```bash
# Run the FFT example
node fft.js

# Run tests
node test_fft.js

# Or use npm scripts
npm start
npm test
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `Math` class for trigonometric functions
- Custom Complex number class
- Automatically pads input to next power of 2
- Node.js 10+ or modern browsers
