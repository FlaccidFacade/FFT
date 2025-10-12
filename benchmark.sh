#!/bin/bash
# Performance comparison script for all FFT implementations

set -e

echo "=============================================="
echo "FFT Multi-Language Performance Comparison"
echo "=============================================="
echo ""
echo "Running benchmarks for 4096 samples..."
echo ""

# Create results file
RESULTS_FILE="performance_results.txt"
echo "Language,Time (ms)" > $RESULTS_FILE

# Python
echo -n "Python...   "
cd python
PYTHON_TIME=$(python3 test_fft.py 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$PYTHON_TIME ms"
echo "Python,$PYTHON_TIME" >> ../$RESULTS_FILE
cd ..

# C++
echo -n "C++...      "
cd cpp
make clean > /dev/null 2>&1
make > /dev/null 2>&1
CPP_TIME=$(./test_fft 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$CPP_TIME ms"
echo "C++,$CPP_TIME" >> ../$RESULTS_FILE
cd ..

# Java
echo -n "Java...     "
cd java
javac FFT.java > /dev/null 2>&1
javac TestFFT.java > /dev/null 2>&1
JAVA_TIME=$(java -ea TestFFT 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$JAVA_TIME ms"
echo "Java,$JAVA_TIME" >> ../$RESULTS_FILE
cd ..

# JavaScript
echo -n "JavaScript..."
cd javascript
JS_TIME=$(node test_fft.js 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$JS_TIME ms"
echo "JavaScript,$JS_TIME" >> ../$RESULTS_FILE
cd ..

# TypeScript
echo -n "TypeScript..."
cd typescript
if [ ! -d "node_modules" ]; then
    npm install > /dev/null 2>&1
fi
TS_TIME=$(npm test 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$TS_TIME ms"
echo "TypeScript,$TS_TIME" >> ../$RESULTS_FILE
cd ..

# Rust
echo -n "Rust...     "
cd rust
RUST_TIME=$(cargo run --release 2>&1 | grep "Performance test passed" | sed 's/.*: \([0-9.]*\) ms.*/\1/')
echo "$RUST_TIME ms"
echo "Rust,$RUST_TIME" >> ../$RESULTS_FILE
cd ..

echo ""
echo "=============================================="
echo "Performance Summary (4096 samples)"
echo "=============================================="
echo ""

# Find fastest
FASTEST=$(awk -F',' 'NR>1 {print $2}' $RESULTS_FILE | sort -n | head -1)

# Print sorted results
echo "Rank | Language    | Time (ms) | Relative"
echo "-----|-------------|-----------|----------"
awk -F',' -v fastest="$FASTEST" '
NR>1 {
    relative = $2 / fastest;
    printf "%-4d | %-11s | %9s | %.2fx\n", NR-1, $1, $2, relative
}' $RESULTS_FILE | sort -t'|' -k3 -n

echo ""
echo "Results saved to: $RESULTS_FILE"
