# Rust FFT Implementation

This implementation uses only Rust standard library (no external crates).

## Building and Running

```bash
# Build
cargo build --release

# Run the FFT example and tests
cargo run --release

# Run unit tests
cargo test

# Run benchmarks
cargo test --release
```

## Implementation Details

- Algorithm: Cooley-Tukey FFT (divide-and-conquer)
- Standard library only: Uses `std::f64::consts::PI` for trigonometric functions
- Custom Complex number struct
- Automatically pads input to next power of 2
- Rust 1.56+ required
