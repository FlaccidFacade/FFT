#!/bin/bash

set -e

# Get the workspace root (works both locally and in container)
WORKSPACE_ROOT="${WORKSPACE_ROOT:-/workspace}"
if [ ! -d "$WORKSPACE_ROOT" ]; then
    WORKSPACE_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
fi

echo "🚀 Setting up FFT Multi-Language Development Environment..."

# Install TypeScript dependencies
echo "📦 Installing TypeScript dependencies..."
cd "$WORKSPACE_ROOT/typescript"
npm install
cd "$WORKSPACE_ROOT"

# Build C++ projects
echo "🔨 Building C++ project..."
cd "$WORKSPACE_ROOT/cpp"
make clean
make
cd "$WORKSPACE_ROOT"

# Build Rust project
echo "🦀 Building Rust project..."
cd "$WORKSPACE_ROOT/rust"
cargo build --release
cd "$WORKSPACE_ROOT"

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
