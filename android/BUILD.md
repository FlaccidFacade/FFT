# Android FFT Visualizer - Build Instructions

## Prerequisites

### Required Software

1. **Android Studio** (Arctic Fox or later)
   - Download from: https://developer.android.com/studio
   - Install with default settings

2. **Android SDK**
   - Minimum SDK: API 34 (Android 14)
   - Target SDK: API 34 (Android 14)
   - Install via Android Studio SDK Manager

3. **NDK (Native Development Kit)**
   - Version: Latest stable (23.1.7779620 or later)
   - Install via Android Studio SDK Manager:
     - Tools → SDK Manager → SDK Tools → NDK (Side by side)

4. **CMake**
   - Version: 3.22.1 or later
   - Install via Android Studio SDK Manager:
     - Tools → SDK Manager → SDK Tools → CMake

5. **JDK (Java Development Kit)**
   - Version: Java 17
   - Usually bundled with Android Studio

## Build Process

### Option 1: Build with Android Studio (Recommended)

1. **Open the Project**
   ```
   File → Open → Select /path/to/FFT/android/
   ```

2. **Sync Gradle**
   - Android Studio will automatically prompt to sync
   - Or manually: File → Sync Project with Gradle Files
   - Wait for sync to complete (may take a few minutes for first build)

3. **Build the Project**
   - Build → Make Project (Ctrl+F9 / Cmd+F9)
   - Or use the toolbar hammer icon

4. **Run on Device/Emulator**
   - Connect an Android device via USB (with USB debugging enabled)
   - Or create an Android Virtual Device (AVD) via AVD Manager
   - Run → Run 'app' (Shift+F10 / Ctrl+R)

### Option 2: Build with Command Line

1. **Navigate to Android Directory**
   ```bash
   cd /path/to/FFT/android
   ```

2. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Output: `app/build/outputs/apk/debug/app-debug.apk`

3. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```
   
   Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

4. **Install on Connected Device**
   ```bash
   ./gradlew installDebug
   ```

5. **Run Tests**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

### Option 3: Build AAB (Android App Bundle) for Play Store

1. **Generate Signed AAB**
   ```bash
   ./gradlew bundleRelease
   ```

2. **Sign the AAB**
   - Create a keystore (first time only):
     ```bash
     keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
     ```
   
   - Sign with jarsigner:
     ```bash
     jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
       -keystore my-release-key.jks \
       app/build/outputs/bundle/release/app-release.aab my-key-alias
     ```

## Troubleshooting

### Common Build Issues

1. **Gradle Sync Failed**
   - Solution: File → Invalidate Caches / Restart
   - Or delete `.gradle` and `.idea` folders, then reopen

2. **NDK Not Found**
   - Solution: Install NDK via SDK Manager
   - Verify path in local.properties: `ndk.dir=/path/to/ndk`

3. **CMake Error**
   - Solution: Install CMake via SDK Manager
   - Or specify version in `android/app/build.gradle.kts`

4. **JNI/Native Library Issues**
   - Check that C++ source files are in correct location
   - Verify CMakeLists.txt paths are correct
   - Clean and rebuild: Build → Clean Project, then Build → Rebuild Project

5. **Dependency Resolution Failed**
   - Check internet connection
   - Clear Gradle cache: `./gradlew clean --refresh-dependencies`
   - Check that jitpack.io is accessible (for MPAndroidChart)

6. **Execution Failed for Task ':app:compileDebugKotlin'**
   - Ensure Kotlin plugin is updated
   - Check that all imports are correct
   - Sync Gradle files

### Device-Specific Issues

1. **App Crashes on Launch**
   - Check logcat for errors: `adb logcat | grep FFT`
   - Verify microphone permission is granted
   - Ensure device runs Android 14+ (API 34)

2. **C++ Library Not Loading**
   - Verify NDK ABI matches device architecture
   - Check that native library is included in APK:
     ```bash
     unzip -l app-debug.apk | grep libfft_native.so
     ```

3. **Audio Capture Not Working**
   - Grant microphone permission in Settings
   - Ensure device has a working microphone
   - Check AudioRecord initialization in logs

## Performance Optimization

### Debug vs Release Builds

- **Debug Build**: Slower, includes debug symbols
  ```bash
  ./gradlew assembleDebug
  ```

- **Release Build**: Optimized, ProGuard/R8 enabled
  ```bash
  ./gradlew assembleRelease
  ```

### Profiling

1. **CPU Profiler**
   - Run → Profile 'app'
   - Record FFT processing performance
   - Identify bottlenecks

2. **Memory Profiler**
   - Monitor memory allocation during audio capture
   - Check for leaks with LeakCanary (if added)

3. **Network Profiler**
   - Not applicable (app doesn't use network)

## Continuous Integration

### GitHub Actions (Future)

Create `.github/workflows/android-build.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
    paths:
      - 'android/**'

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x android/gradlew
      - name: Build with Gradle
        run: |
          cd android
          ./gradlew assembleDebug
      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: android/app/build/outputs/apk/debug/app-debug.apk
```

## Deployment

### Play Store Internal Testing

1. Build signed AAB
2. Upload to Play Console
3. Create internal testing track
4. Add testers via email
5. Share test link

### Direct APK Distribution

1. Build debug APK: `./gradlew assembleDebug`
2. Share APK file: `app/build/outputs/apk/debug/app-debug.apk`
3. Recipients must enable "Install from Unknown Sources"

## Development Tips

1. **Use Gradle Daemon**: Speeds up builds
   ```bash
   echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties
   ```

2. **Parallel Builds**: Enable in gradle.properties
   ```
   org.gradle.parallel=true
   ```

3. **Build Cache**: Enable Gradle build cache
   ```
   org.gradle.caching=true
   ```

4. **Use ADB Wireless**: No USB cable needed (Android 11+)
   ```bash
   adb pair <device-ip>:port
   adb connect <device-ip>:port
   ```

## Support

For issues:
1. Check GitHub Issues: https://github.com/FlaccidFacade/FFT/issues
2. Review Android documentation: https://developer.android.com
3. Stack Overflow: Tag with `android`, `fft`, `android-ndk`

## References

- [Android Developer Guide](https://developer.android.com/guide)
- [NDK Documentation](https://developer.android.com/ndk)
- [Gradle Build Tool](https://gradle.org/)
- [Kotlin Language](https://kotlinlang.org/)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
