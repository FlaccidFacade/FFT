#!/bin/bash

set -e

echo "🚀 Setting up FFT Multi-Language Development Environment..."

# Install TypeScript dependencies
echo "📦 Installing TypeScript dependencies..."
cd /workspace/typescript
npm install
cd /workspace

# Build C++ projects
echo "🔨 Building C++ project..."
cd /workspace/cpp
make clean
make
cd /workspace

# Build Rust project
echo "🦀 Building Rust project..."
cd /workspace/rust
cargo build --release
cd /workspace

# Display versions
echo ""
echo "✅ Environment ready! Installed versions:"
echo "  Python: $(python3 --version)"
echo "  Node.js: $(node --version)"
echo "  npm: $(npm --version)"
echo "  Java: $(java -version 2>&1 | head -n 1)"
echo "  g++: $(g++ --version | head -n 1)"
echo "  Rust: $(rustc --version)"
echo ""
echo "🎉 You can now start debugging with one click!"
echo "   Use the Run and Debug panel (Ctrl+Shift+D) to select a configuration."
