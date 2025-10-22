# Quick Start Guide: GitHub Codespaces

Get up and running with the FFT Multi-Language project in under 5 minutes using GitHub Codespaces!

## 🚀 Steps

### 1. Create a Codespace

1. Go to the [FFT repository](https://github.com/FlaccidFacade/FFT) on GitHub
2. Click the green **"Code"** button
3. Select the **"Codespaces"** tab
4. Click **"Create codespace on main"** (or choose a different branch)

![Create Codespace](https://docs.github.com/assets/cb-77061/mw-1440/images/help/codespaces/new-codespace-button.webp)

### 2. Wait for Setup

The container will build and configure automatically (first time: 3-5 minutes):

- ✅ Ubuntu 22.04 container with all language runtimes
- ✅ Python 3.12, Node.js 20, Java 17, C++, Rust installed
- ✅ TypeScript dependencies installed
- ✅ C++ and Rust projects pre-built
- ✅ VSCode extensions loaded
- ✅ Debug configurations ready

You'll see output in the terminal showing the setup progress.

### 3. Explore the Project

Once the codespace opens:

- **File Explorer** (left sidebar): Browse all language implementations
- **Terminal** (bottom): Pre-configured with all tools available
- **Extensions**: All recommended extensions are installed

### 4. Run Tests

In the terminal, run:

```bash
./run_tests.sh
```

This will execute tests for all 6 languages (Python, C++, Java, JavaScript, TypeScript, Rust).

### 5. Start Debugging

This is where the magic happens! **One-click debugging** for any language:

1. Press **`Ctrl+Shift+D`** (Windows/Linux) or **`Cmd+Shift+D`** (Mac)
2. In the Debug panel, select a configuration from the dropdown:
   - **Python: FFT Main** or **Python: Run Tests**
   - **C++: FFT Main** or **C++: Run Tests**
   - **Java: FFT Main** or **Java: Run Tests**
   - **JavaScript: FFT Main** or **JavaScript: Run Tests**
   - **TypeScript: FFT Main** or **TypeScript: Run Tests**
   - **Rust: FFT Main** or **Rust: Run Tests**
3. Press **`F5`** or click the green ▶️ play button
4. Set breakpoints by clicking left of line numbers
5. Debug with full IntelliSense and variable inspection!

![Debug Panel](https://code.visualstudio.com/assets/docs/editor/debugging/debugging_hero.png)

## 💡 Quick Tips

### Running Individual Language Tests

```bash
# Python
cd python && python3 test_fft.py

# C++
cd cpp && make && ./test_fft

# Java
cd java && javac FFT.java TestFFT.java && java -ea TestFFT

# JavaScript
cd javascript && node test_fft.js

# TypeScript
cd typescript && npm test

# Rust
cd rust && cargo test --release
```

### Building Projects

Use VS Code tasks:
- Press **`Ctrl+Shift+P`** (or **`Cmd+Shift+P`**)
- Type "Tasks: Run Task"
- Select from:
  - Build C++ FFT
  - Build TypeScript
  - Build Rust FFT
  - Run All Tests
  - Language-specific test tasks

Or press **`Ctrl+Shift+B`** for the build menu.

### Setting Breakpoints

1. Open any source file (e.g., `python/fft.py`)
2. Click left of the line number to add a red dot (breakpoint)
3. Select the appropriate debug configuration
4. Press **`F5`** to start debugging
5. Execution will pause at your breakpoint!

### Viewing Variables

While debugging:
- **Variables panel**: See all local variables
- **Watch panel**: Add expressions to monitor
- **Call Stack**: Navigate the execution stack
- **Debug Console**: Execute code in the current context

## 🎯 Example: Debug Python FFT

1. Open `python/test_fft.py`
2. Set a breakpoint on line 15 (inside the `test_correctness()` function)
3. Open Debug panel (**`Ctrl+Shift+D`**)
4. Select **"Python: Run Tests"**
5. Press **`F5`**
6. Execution pauses at line 15
7. Inspect the `signal` variable in the Variables panel
8. Step through with **`F10`** (step over) or **`F11`** (step into)

## 🔧 Customizing Your Environment

### Add Python Packages

```bash
pip install --user package-name
```

### Install Additional VSCode Extensions

Click the Extensions icon (left sidebar) and search/install.

### Modify Settings

- **User Settings**: Settings that persist across all Codespaces
- **Workspace Settings**: `.vscode/settings.json` (shared with team)

## 📚 Learn More

- [DEVCONTAINER.md](DEVCONTAINER.md) - Full devcontainer documentation
- [README.md](README.md) - Project overview
- [GitHub Codespaces Docs](https://docs.github.com/en/codespaces)

## ⚡ Performance Tips

- **Forward Ports**: If you run a server, it auto-forwards (see Ports panel)
- **Rebuild Container**: If you change `.devcontainer/*`, rebuild via Command Palette
- **Stop Codespace**: When done, stop it to save compute time (Settings → Codespaces)
- **Secrets**: Store API keys in GitHub Settings → Codespaces → Secrets

## 🆘 Troubleshooting

### Container Won't Build
- Check the Terminal for build logs
- Try: Command Palette → "Codespaces: Rebuild Container"

### Extension Not Working
- Ensure it loaded: Check Extensions panel
- Try: Command Palette → "Developer: Reload Window"

### Debug Not Starting
- Verify the build succeeded (check Terminal)
- Ensure files are saved before debugging
- Check Debug Console for errors

### Need to Install Something?
```bash
# System packages (requires sudo)
sudo apt-get update && sudo apt-get install -y package-name

# Python packages
pip install --user package-name

# Node packages (global)
npm install -g package-name
```

## 🎉 You're All Set!

You now have a fully configured, cloud-based development environment with:
- ✅ All 6 programming languages ready to use
- ✅ One-click debugging for any language
- ✅ Consistent formatting and linting
- ✅ All tests passing
- ✅ No local setup required!

Happy coding! 🚀
