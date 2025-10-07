#!/bin/bash
# Unified test runner for all FFT implementations

set -e

echo "=============================================="
echo "FFT Multi-Language Test Suite"
echo "=============================================="
echo ""

EXIT_CODE=0

# Python
echo "📦 Testing Python FFT..."
cd python
python3 test_fft.py
PYTHON_STATUS=$?
cd ..
if [ $PYTHON_STATUS -eq 0 ]; then
    echo "✅ Python tests passed"
else
    echo "❌ Python tests failed"
    EXIT_CODE=1
fi
echo ""

# C++
echo "📦 Testing C++ FFT..."
cd cpp
make clean > /dev/null 2>&1
make > /dev/null 2>&1
./test_fft
CPP_STATUS=$?
cd ..
if [ $CPP_STATUS -eq 0 ]; then
    echo "✅ C++ tests passed"
else
    echo "❌ C++ tests failed"
    EXIT_CODE=1
fi
echo ""

# Java
echo "📦 Testing Java FFT..."
cd java
javac FFT.java
javac TestFFT.java
java -ea TestFFT
JAVA_STATUS=$?
cd ..
if [ $JAVA_STATUS -eq 0 ]; then
    echo "✅ Java tests passed"
else
    echo "❌ Java tests failed"
    EXIT_CODE=1
fi
echo ""

# JavaScript
echo "📦 Testing JavaScript FFT..."
cd javascript
node test_fft.js
JS_STATUS=$?
cd ..
if [ $JS_STATUS -eq 0 ]; then
    echo "✅ JavaScript tests passed"
else
    echo "❌ JavaScript tests failed"
    EXIT_CODE=1
fi
echo ""

# TypeScript
echo "📦 Testing TypeScript FFT..."
cd typescript
if [ ! -d "node_modules" ]; then
    npm install > /dev/null 2>&1
fi
npm test
TS_STATUS=$?
cd ..
if [ $TS_STATUS -eq 0 ]; then
    echo "✅ TypeScript tests passed"
else
    echo "❌ TypeScript tests failed"
    EXIT_CODE=1
fi
echo ""

# Rust
echo "📦 Testing Rust FFT..."
cd rust
cargo test --release > /dev/null 2>&1
RUST_STATUS=$?
cd ..
if [ $RUST_STATUS -eq 0 ]; then
    echo "✅ Rust tests passed"
else
    echo "❌ Rust tests failed"
    EXIT_CODE=1
fi
echo ""

echo "=============================================="
if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ ALL TESTS PASSED"
else
    echo "❌ SOME TESTS FAILED"
fi
echo "=============================================="

exit $EXIT_CODE
