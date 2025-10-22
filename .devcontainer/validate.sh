#!/bin/bash
# Validation script to test the devcontainer configuration
# This script can be run to verify that the setup is working correctly

set -e

echo "=================================================="
echo "Codespaces Configuration Validation Script"
echo "=================================================="
echo ""

# Color codes for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0
WARNINGS=0

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to print success
success() {
    echo -e "${GREEN}✓${NC} $1"
}

# Function to print error
error() {
    echo -e "${RED}✗${NC} $1"
    ((ERRORS++))
}

# Function to print warning
warning() {
    echo -e "${YELLOW}!${NC} $1"
    ((WARNINGS++))
}

echo "1. Checking Language Runtimes..."
echo "-----------------------------------"

# Python
if command_exists python3; then
    VERSION=$(python3 --version 2>&1)
    success "Python 3 installed: $VERSION"
else
    error "Python 3 not found"
fi

# Node.js
if command_exists node; then
    VERSION=$(node --version)
    success "Node.js installed: $VERSION"
else
    error "Node.js not found"
fi

# npm
if command_exists npm; then
    VERSION=$(npm --version)
    success "npm installed: $VERSION"
else
    error "npm not found"
fi

# Java
if command_exists java; then
    VERSION=$(java -version 2>&1 | head -n 1)
    success "Java installed: $VERSION"
else
    error "Java not found"
fi

# javac
if command_exists javac; then
    VERSION=$(javac -version 2>&1)
    success "Java compiler installed: $VERSION"
else
    error "Java compiler not found"
fi

# g++
if command_exists g++; then
    VERSION=$(g++ --version | head -n 1)
    success "g++ installed: $VERSION"
else
    error "g++ not found"
fi

# Rust
if command_exists rustc; then
    VERSION=$(rustc --version)
    success "Rust installed: $VERSION"
else
    error "Rust not found"
fi

# Cargo
if command_exists cargo; then
    VERSION=$(cargo --version)
    success "Cargo installed: $VERSION"
else
    error "Cargo not found"
fi

echo ""
echo "2. Checking Development Tools..."
echo "-----------------------------------"

# git
if command_exists git; then
    VERSION=$(git --version)
    success "Git installed: $VERSION"
else
    error "Git not found"
fi

# make
if command_exists make; then
    VERSION=$(make --version | head -n 1)
    success "Make installed: $VERSION"
else
    error "Make not found"
fi

# gdb
if command_exists gdb; then
    VERSION=$(gdb --version 2>&1 | head -n 1)
    success "GDB installed: $VERSION"
else
    warning "GDB not found (optional for C++ debugging)"
fi

echo ""
echo "3. Checking Configuration Files..."
echo "-----------------------------------"

# devcontainer.json
if [ -f ".devcontainer/devcontainer.json" ]; then
    success "devcontainer.json exists"
else
    error "devcontainer.json not found"
fi

# Dockerfile
if [ -f ".devcontainer/Dockerfile" ]; then
    success "Dockerfile exists"
else
    error "Dockerfile not found"
fi

# post-create.sh
if [ -f ".devcontainer/post-create.sh" ]; then
    if [ -x ".devcontainer/post-create.sh" ]; then
        success "post-create.sh exists and is executable"
    else
        warning "post-create.sh exists but is not executable"
    fi
else
    error "post-create.sh not found"
fi

# VSCode settings
if [ -f ".vscode/settings.json" ]; then
    success "settings.json exists"
else
    error "settings.json not found"
fi

# Launch configurations
if [ -f ".vscode/launch.json" ]; then
    success "launch.json exists"
else
    error "launch.json not found"
fi

# Tasks
if [ -f ".vscode/tasks.json" ]; then
    success "tasks.json exists"
else
    error "tasks.json not found"
fi

# Extensions
if [ -f ".vscode/extensions.json" ]; then
    success "extensions.json exists"
else
    error "extensions.json not found"
fi

# C++ properties
if [ -f ".vscode/c_cpp_properties.json" ]; then
    success "c_cpp_properties.json exists"
else
    warning "c_cpp_properties.json not found (optional)"
fi

echo ""
echo "4. Checking Project Structure..."
echo "-----------------------------------"

LANGUAGES=("python" "cpp" "java" "javascript" "typescript" "rust")
for lang in "${LANGUAGES[@]}"; do
    if [ -d "$lang" ]; then
        success "$lang directory exists"
    else
        error "$lang directory not found"
    fi
done

echo ""
echo "5. Testing Builds..."
echo "-----------------------------------"

# Test TypeScript dependencies
if [ -f "typescript/package.json" ]; then
    if [ -d "typescript/node_modules" ]; then
        success "TypeScript dependencies installed"
    else
        warning "TypeScript dependencies not installed (run: cd typescript && npm install)"
    fi
else
    error "TypeScript package.json not found"
fi

# Test C++ build
if [ -f "cpp/Makefile" ]; then
    success "C++ Makefile exists"
    if [ -f "cpp/test_fft" ]; then
        success "C++ test executable found"
    else
        warning "C++ test executable not found (run: cd cpp && make)"
    fi
else
    error "C++ Makefile not found"
fi

# Test Rust build
if [ -f "rust/Cargo.toml" ]; then
    success "Rust Cargo.toml exists"
    if [ -d "rust/target" ]; then
        success "Rust has been built"
    else
        warning "Rust not built yet (run: cd rust && cargo build)"
    fi
else
    error "Rust Cargo.toml not found"
fi

echo ""
echo "6. Documentation..."
echo "-----------------------------------"

if [ -f "README.md" ]; then
    success "README.md exists"
else
    error "README.md not found"
fi

if [ -f "DEVCONTAINER.md" ]; then
    success "DEVCONTAINER.md exists"
else
    warning "DEVCONTAINER.md not found"
fi

if [ -f "QUICKSTART.md" ]; then
    success "QUICKSTART.md exists"
else
    warning "QUICKSTART.md not found"
fi

echo ""
echo "=================================================="
echo "Validation Summary"
echo "=================================================="

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed!${NC}"
    echo ""
    echo "Your environment is fully configured and ready to use."
    echo "Start debugging with: Ctrl+Shift+D and press F5"
    exit 0
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}⚠ All critical checks passed with $WARNINGS warning(s)${NC}"
    echo ""
    echo "Your environment is functional but some optional components are missing."
    echo "Start debugging with: Ctrl+Shift+D and press F5"
    exit 0
else
    echo -e "${RED}✗ $ERRORS error(s) and $WARNINGS warning(s) found${NC}"
    echo ""
    echo "Please fix the errors above before proceeding."
    exit 1
fi
