# GitHub Codespaces Development Container

This repository is configured with a GitHub Codespaces development container that provides a complete, ready-to-use development environment for the FFT multi-language project.

## 🚀 Quick Start

1. **Create a Codespace**
   - Click the green "Code" button on GitHub
   - Select the "Codespaces" tab
   - Click "Create codespace on main" (or your branch)
   
2. **Wait for Setup**
   - The container will build automatically (first time takes ~3-5 minutes)
   - Dependencies are installed via the post-create script
   - All languages and tools will be ready to use

3. **Start Debugging**
   - Press `Ctrl+Shift+D` (or `Cmd+Shift+D` on Mac) to open the Debug panel
   - Select any debug configuration from the dropdown
   - Press `F5` or click the green play button to start debugging

## 📦 What's Included

### Languages and Runtimes

- **Python 3.12** - Latest Python with pip
- **C++ (g++ with C++11)** - GCC compiler with debugging support (gdb)
- **Java 17** - OpenJDK for Java development
- **Node.js 20** - For JavaScript and TypeScript
- **Rust (latest stable)** - Cargo and rustc

### Development Tools

- **Build Tools**: make, cmake, cargo, npm
- **Debuggers**: gdb (C++), lldb (Rust), debugpy (Python)
- **Utilities**: git, vim, nano, curl, wget

### VSCode Extensions

The following extensions are automatically installed:

#### Python
- Python language support
- Pylance (IntelliSense)
- Python debugger

#### C++
- C/C++ IntelliSense
- C/C++ debugging
- CMake Tools

#### Java
- Java Extension Pack
- Java debugger

#### JavaScript/TypeScript
- ESLint
- Prettier
- TypeScript language support

#### Rust
- rust-analyzer (language server)
- LLDB debugger

#### General
- EditorConfig
- GitLens
- GitHub Actions

## 🐛 Debug Configurations

The `.vscode/launch.json` file includes pre-configured debug settings for all languages:

### Python
- **Python: FFT Main** - Debug the main FFT implementation
- **Python: Run Tests** - Debug the test suite

### C++
- **C++: FFT Main** - Debug the main FFT executable
- **C++: Run Tests** - Debug the test executable

### Java
- **Java: FFT Main** - Debug the main FFT class
- **Java: Run Tests** - Debug the test class

### JavaScript
- **JavaScript: FFT Main** - Debug the FFT implementation
- **JavaScript: Run Tests** - Debug the test suite

### TypeScript
- **TypeScript: FFT Main** - Debug compiled TypeScript FFT
- **TypeScript: Run Tests** - Debug compiled TypeScript tests

### Rust
- **Rust: FFT Main** - Debug the Rust FFT binary
- **Rust: Run Tests** - Debug Rust test binaries

## 🔧 Build Tasks

The `.vscode/tasks.json` file provides build and test tasks:

- **Build C++ FFT** - Compile C++ code
- **Build TypeScript** - Compile TypeScript to JavaScript
- **Build Rust FFT** - Build Rust project
- **Run All Tests** - Execute tests for all languages
- Individual test tasks for each language

Access tasks via:
- `Ctrl+Shift+B` (or `Cmd+Shift+B` on Mac) for build tasks
- `Ctrl+Shift+P` → "Tasks: Run Task" for all tasks

## 🎨 Editor Settings

Consistent formatting and linting across the team:

- **Auto-save** enabled with delay
- **Format on save** enabled
- Language-specific tab sizes (4 for Python/C++/Java/Rust, 2 for JS/TS)
- Type checking enabled for Python and TypeScript

## 📁 File Structure

```
.devcontainer/
├── devcontainer.json    # Main configuration file
├── Dockerfile           # Custom container image
└── post-create.sh       # Automatic setup script

.vscode/
├── launch.json          # Debug configurations
├── tasks.json           # Build and test tasks
├── settings.json        # Workspace settings
└── extensions.json      # Recommended extensions
```

## 🔄 Updating Dependencies

### Adding New System Packages

Edit `.devcontainer/Dockerfile` and add packages to the `apt-get install` commands:

```dockerfile
RUN apt-get update && apt-get install -y \
    existing-package \
    new-package \
    && rm -rf /var/lib/apt/lists/*
```

### Adding VSCode Extensions

Edit `.devcontainer/devcontainer.json` in the `customizations.vscode.extensions` array:

```json
"extensions": [
    "existing.extension",
    "publisher.new-extension"
]
```

### Updating Language Versions

#### Python
Edit the Dockerfile to change the Python version:
```dockerfile
RUN add-apt-repository ppa:deadsnakes/ppa -y && \
    apt-get update && \
    apt-get install -y python3.13 python3.13-dev ...
```

#### Node.js
Edit the Dockerfile to change the Node.js version:
```dockerfile
RUN curl -fsSL https://deb.nodesource.com/setup_21.x | bash - && \
    apt-get install -y nodejs
```

#### Java
Edit the Dockerfile to change the Java version:
```dockerfile
RUN apt-get install -y openjdk-21-jdk
```

#### Rust
Rust uses the latest stable version by default. To use a specific version:
```dockerfile
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y --default-toolchain 1.75.0
```

### Modifying Post-Create Script

The `.devcontainer/post-create.sh` script runs after container creation. Edit it to:
- Install additional dependencies
- Set up databases
- Pre-build projects
- Initialize services

Example additions:
```bash
# Install additional Python packages
pip install --user numpy scipy

# Set up a database
sudo apt-get update && sudo apt-get install -y postgresql
```

## 🔍 Troubleshooting

### Container Build Fails

1. Check the Dockerfile syntax
2. Ensure all URLs are accessible
3. Look at the build logs in the terminal
4. Try rebuilding: "Codespaces: Rebuild Container"

### Extensions Not Loading

1. Rebuild the container
2. Check the extension IDs in `devcontainer.json`
3. Ensure extensions are compatible with the VSCode version

### Debug Configuration Not Working

1. Verify the build task completed successfully
2. Check paths in `launch.json` match your project structure
3. Ensure the debugger extension is installed
4. Review the Debug Console for error messages

### Post-Create Script Fails

1. Check script permissions: `chmod +x .devcontainer/post-create.sh`
2. Review the script for syntax errors
3. Check the creation log in the terminal
4. Ensure all dependencies are available

## 🛠️ Manual Setup (Without Codespaces)

If you want to use this devcontainer locally with VS Code:

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop)
2. Install the [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
3. Open the repository in VS Code
4. Press `F1` and select "Dev Containers: Reopen in Container"

## 📚 Additional Resources

- [GitHub Codespaces Documentation](https://docs.github.com/en/codespaces)
- [Dev Containers Specification](https://containers.dev/)
- [VSCode Debugging Guide](https://code.visualstudio.com/docs/editor/debugging)
- [VSCode Tasks Guide](https://code.visualstudio.com/docs/editor/tasks)

## 🤝 Contributing

When contributing to the devcontainer configuration:

1. Test changes in a new Codespace before committing
2. Document any new dependencies or requirements
3. Update this README if you add new features
4. Ensure the post-create script remains idempotent
5. Keep container build time reasonable (< 10 minutes)

## 💡 Tips

- Use `Ctrl+Shift+P` (or `Cmd+Shift+P`) to access the command palette
- Forward ports automatically by running servers (they appear in the Ports panel)
- Customize your personal Codespace settings in GitHub Settings → Codespaces
- Use `code .` in the terminal to open files/folders in the editor
- Rebuild the container after changing `devcontainer.json` or `Dockerfile`
