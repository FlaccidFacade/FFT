# FFT - Multi-Language Fast Fourier Transform Implementation

[![CI Status](https://github.com/FlaccidFacade/FFT/workflows/FFT%20Multi-Language%20CI/badge.svg)](https://github.com/FlaccidFacade/FFT/actions)
[![Python](https://img.shields.io/badge/Python-3.12-blue.svg)](https://www.python.org/)
[![C++](https://img.shields.io/badge/C++-11-00599C.svg)](https://isocpp.org/)
[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://www.oracle.com/java/)
[![JavaScript](https://img.shields.io/badge/JavaScript-ES2020-yellow.svg)](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue.svg)](https://www.typescriptlang.org/)
[![Rust](https://img.shields.io/badge/Rust-1.56+-orange.svg)](https://www.rust-lang.org/)

A collection of Fast Fourier Transform (FFT) implementations across six programming languages: **Python**, **C++**, **Java**, **JavaScript**, **TypeScript**, and **Rust**. Each implementation uses only standard library functions and includes comprehensive test suites with performance benchmarks.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Language-Specific Details](#language-specific-details)
- [Performance Comparison](#performance-comparison)
- [Algorithm](#algorithm)
- [CI/CD](#cicd)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This project implements the Cooley-Tukey FFT algorithm—a divide-and-conquer algorithm that computes the Discrete Fourier Transform (DFT) in O(N log N) time. Each language implementation:

- Uses **only standard library** (no external dependencies for core FFT)
- Includes **three comprehensive tests**:
  1. **Correctness Test**: Validates FFT output against known values
  2. **Edge Cases Test**: Tests empty inputs, single elements, power-of-2 and non-power-of-2 lengths
  3. **Performance Benchmark**: Measures execution time for various input sizes
- Automatically pads inputs to the next power of 2
- Produces executable artifacts

## ✨ Features

- 🌐 **Multi-language**: Six complete implementations
- 📊 **Performance benchmarks**: Compare execution times across languages
- ✅ **Comprehensive testing**: Three test categories for each language
- 🔧 **Standard library only**: No external dependencies for FFT computation
- 🚀 **CI/CD ready**: GitHub Actions workflow included
- 📈 **Automatic padding**: Handles non-power-of-2 input sizes

## 📁 Project Structure

```
FFT/
├── python/           # Python implementation
│   ├── fft.py
│   ├── test_fft.py
│   └── README.md
├── cpp/              # C++ implementation
│   ├── fft.h
│   ├── fft_impl.cpp
│   ├── fft.cpp
│   ├── test_fft.cpp
│   ├── Makefile
│   └── README.md
├── java/             # Java implementation
│   ├── FFT.java
│   ├── TestFFT.java
│   └── README.md
├── javascript/       # JavaScript implementation
│   ├── fft.js
│   ├── test_fft.js
│   ├── package.json
│   └── README.md
├── typescript/       # TypeScript implementation
│   ├── fft.ts
│   ├── test_fft.ts
│   ├── package.json
│   ├── tsconfig.json
│   └── README.md
├── rust/             # Rust implementation
│   ├── src/
│   │   ├── lib.rs
│   │   └── main.rs
│   ├── Cargo.toml
│   └── README.md
├── .github/
│   └── workflows/
│       └── ci.yml    # CI/CD configuration
├── run_tests.sh      # Unified test runner
└── README.md
```

## 🚀 Installation

### Prerequisites

Make sure you have the following installed:

- **Python 3.12+**
- **C++ compiler** (g++ with C++11 support)
- **Java 17+** (JDK)
- **Node.js 20+** (for JavaScript and TypeScript)
- **Rust 1.56+** (with cargo)

### Clone the Repository

```bash
git clone https://github.com/FlaccidFacade/FFT.git
cd FFT
```

## 🧪 Running Tests

### Run All Tests (All Languages)

Use the unified test runner to run tests for all languages:

```bash
./run_tests.sh
```

### Run Tests Per Language

#### Python
```bash
cd python
python3 test_fft.py
```

#### C++
```bash
cd cpp
make
./test_fft
```

#### Java
```bash
cd java
javac FFT.java TestFFT.java
java -ea TestFFT
```

#### JavaScript
```bash
cd javascript
node test_fft.js
```

#### TypeScript
```bash
cd typescript
npm install
npm test
```

#### Rust
```bash
cd rust
cargo test --release
# or run with output
cargo run --release
```

## 📚 Language-Specific Details

### Python
- **Standard library**: `cmath` for complex numbers
- **Type hints**: Fully typed
- **Requirements**: None (standard library only)

### C++
- **Standard**: C++11
- **Libraries**: `<complex>`, `<vector>`, `<cmath>`
- **Build system**: Makefile

### Java
- **Version**: Java 8+
- **Features**: Custom Complex class, no external dependencies
- **Assertions**: Enabled with `-ea` flag

### JavaScript
- **Standard**: ES2020
- **Runtime**: Node.js 10+
- **Module system**: CommonJS

### TypeScript
- **Version**: TypeScript 5.0+
- **Target**: ES2020
- **Type safety**: Strict mode enabled

### Rust
- **Edition**: 2021
- **Features**: Zero-cost abstractions, memory safety
- **Build**: Cargo with release optimizations

## ⚡ Performance Comparison

Performance benchmarks are run automatically for 4096 samples. Here are typical results:

| Language    | Time (ms) | Relative Speed |
|-------------|-----------|----------------|
| Rust        | 0.91      | 1.00x (fastest)|
| C++         | 1.00      | 1.09x          |
| Java        | 2.63      | 2.88x          |
| JavaScript  | 4.11      | 4.49x          |
| TypeScript  | 4.45      | 4.86x          |
| Python      | 10.99     | 12.02x         |

*Note: Actual performance varies by hardware and system load.*

### Running Benchmarks

```bash
# Run comprehensive performance comparison
./benchmark.sh
```

### Performance Testing

Each implementation includes performance benchmarks for input sizes:
- 64 samples
- 256 samples
- 1024 samples
- 4096 samples

## 🔬 Algorithm

All implementations use the **Cooley-Tukey FFT algorithm**:

1. **Divide**: Split input into even and odd indexed elements
2. **Conquer**: Recursively compute FFT of both halves
3. **Combine**: Merge results using twiddle factors

**Time Complexity**: O(N log N)  
**Space Complexity**: O(N)

### Pseudocode

```
function FFT(x):
    N = length(x)
    if N ≤ 1:
        return x
    
    even = FFT(x[0, 2, 4, ...])
    odd = FFT(x[1, 3, 5, ...])
    
    result = array of size N
    for k from 0 to N/2 - 1:
        t = exp(-2πi * k/N) * odd[k]
        result[k] = even[k] + t
        result[k + N/2] = even[k] - t
    
    return result
```

## 🔄 CI/CD

The project uses GitHub Actions for continuous integration:

- **Automated testing**: All language tests run on each push
- **Performance benchmarking**: Compares execution times across languages
- **Multiple platforms**: Tested on Ubuntu (can be extended to Windows/macOS)
- **Artifact upload**: Performance results saved for each run

See [`.github/workflows/ci.yml`](.github/workflows/ci.yml) for details.

### Badges

The README includes badges for:
- CI/CD status
- Language versions
- Code coverage (when configured with CodeCov)

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Add tests for new features
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

MIT License - feel free to use this code for learning or in your projects.

## 🙏 Acknowledgments

- Cooley-Tukey FFT algorithm (1965)
- Standard library maintainers of all languages
- Open source community

## 📖 Further Reading

- [FFT on Wikipedia](https://en.wikipedia.org/wiki/Fast_Fourier_transform)
- [Cooley-Tukey Algorithm](https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm)
- [Digital Signal Processing](https://en.wikipedia.org/wiki/Digital_signal_processing)
