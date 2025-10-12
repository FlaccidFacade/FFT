# TypeScript FFT Implementation

This implementation uses only TypeScript standard library (no external dependencies beyond type definitions).

## Building and Running

```bash
# Install dependencies (TypeScript compiler and type definitions)
npm install

# Build
npm run build

# Run the FFT example
npm start

# Run tests
npm test
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `Math` class for trigonometric functions
- Custom Complex number class with type safety
- Automatically pads input to next power of 2
- TypeScript 5.0+ and Node.js 10+ required
