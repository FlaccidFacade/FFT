# Android FFT Visualizer App

An Android application that captures live audio, performs FFT analysis using multiple language implementations (C++, Java, Python), and provides real-time visualization with performance benchmarking.

## Features

- ✅ **Real-time Audio Capture**: Uses Android's AudioRecord API for low-latency microphone input
- ✅ **Multi-Language FFT Processing**: Runs FFT implementations from:
  - C++ (via JNI/NDK)
  - Java (native Kotlin port)
  - Python (using Java as fallback - Python JNI integration is complex)
- ✅ **Live Visualization**: Grid layout displaying three simultaneous FFT visualizations
  - Waveform view (time domain)
  - Frequency spectrum view (frequency domain)
  - Real-time FPS and latency metrics
- ✅ **Performance Benchmarking**:
  - Processing speed measurement per implementation
  - FPS tracking
  - Frequency detection accuracy
  - Divergence calculation between implementations
- ✅ **Data Export**: CSV export of all collected metrics
- ✅ **Performance Charts**: Line charts showing metrics over time using MPAndroidChart

## Requirements

- Android SDK 34+ (Android 14+)
- Gradle 8.2+
- NDK (for C++ FFT compilation)
- Kotlin 1.9.20+

## Target Devices

- Samsung Galaxy S25 Ultra
- Samsung Tab S10+
- Any Android device running Android 14+

## Project Structure

```
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flaccidfacade/fftvisualizer/
│   │   │   │   ├── MainActivity.kt              # Main activity with grid layout
│   │   │   │   ├── PerformanceChartActivity.kt  # Charts activity
│   │   │   │   ├── fft/
│   │   │   │   │   ├── JavaFFT.kt              # Java FFT implementation
│   │   │   │   │   └── NativeFFT.kt            # JNI wrapper for C++ FFT
│   │   │   │   ├── view/
│   │   │   │   │   ├── WaveformView.kt         # Custom waveform view
│   │   │   │   │   └── FrequencyView.kt        # Custom frequency view
│   │   │   │   ├── audio/
│   │   │   │   │   └── AudioCaptureManager.kt  # Audio capture logic
│   │   │   │   ├── data/
│   │   │   │   │   └── FFTMetrics.kt           # Metrics data classes
│   │   │   │   └── utils/
│   │   │   │       ├── CSVExporter.kt          # CSV export utility
│   │   │   │       └── FFTUtils.kt             # FFT utility functions
│   │   │   ├── cpp/
│   │   │   │   ├── CMakeLists.txt              # CMake build configuration
│   │   │   │   └── fft_jni.cpp                 # JNI wrapper for C++ FFT
│   │   │   ├── res/                             # Android resources
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Building the App

### Prerequisites

1. Install Android Studio Arctic Fox or later
2. Install Android SDK 34
3. Install NDK (via SDK Manager)
4. Install CMake (via SDK Manager)

### Build Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/FlaccidFacade/FFT.git
   cd FFT/android
   ```

2. **Open in Android Studio**:
   - File → Open → Select the `android` directory

3. **Sync Gradle**:
   - Android Studio will automatically sync Gradle dependencies

4. **Build the project**:
   ```bash
   ./gradlew build
   ```

5. **Install on device**:
   ```bash
   ./gradlew installDebug
   ```

   Or use Android Studio's "Run" button.

## Usage

### Starting Audio Capture

1. Launch the app
2. Grant microphone permission when prompted
3. Tap "Start Audio Capture" button
4. Speak, play music, or generate sound near the device microphone
5. Observe real-time FFT visualizations for each implementation

### Viewing Performance Charts

1. Tap "View Performance Charts" button
2. View three charts:
   - Processing speed over time
   - Detected frequency accuracy
   - Implementation divergence

### Exporting Data

1. Tap "Export CSV Data" button
2. Data is saved to app-specific storage: `Android/data/com.flaccidfacade.fftvisualizer/files/Documents/`
3. Access via file manager or ADB:
   ```bash
   adb pull /sdcard/Android/data/com.flaccidfacade.fftvisualizer/files/Documents/
   ```

## Architecture

### Audio Processing Pipeline

1. **AudioCaptureManager**: Captures PCM audio data from microphone at 44.1kHz
2. **Windowing**: Applies Hamming window to reduce spectral leakage
3. **FFT Processing**: Computes FFT using three implementations:
   - C++ (native via JNI)
   - Java (Kotlin port)
   - Python (Java fallback)
4. **Frequency Analysis**: Detects dominant frequency and peak magnitude
5. **Metrics Collection**: Records processing time, FPS, and accuracy
6. **Visualization**: Updates waveform and frequency views in real-time

### Performance Optimization

- **Coroutines**: Audio processing runs on background threads
- **Native Code**: C++ FFT provides lowest latency
- **Efficient Rendering**: Custom views minimize overdraw
- **Buffering**: Audio buffer size optimized for real-time performance

## Dependencies

- **AndroidX**: Core Android libraries
- **Kotlin Coroutines**: Asynchronous programming
- **MPAndroidChart**: Chart visualization
- **Oboe**: Low-latency audio (library available, not yet integrated)
- **OpenCSV**: CSV file export

## Known Limitations

1. **Python FFT**: Currently uses Java implementation as fallback. Full Python integration via Chaquopy or similar would require additional setup.
2. **Storage Permissions**: On Android 14+, app-specific storage is used (no broad storage permission needed)
3. **Background Processing**: Audio capture stops when app is backgrounded

## Testing

### Manual Testing Checklist

- [ ] Microphone permission request/grant
- [ ] Audio capture start/stop
- [ ] Real-time waveform visualization
- [ ] Real-time frequency visualization
- [ ] FPS counter updates
- [ ] Latency measurement displays correctly
- [ ] Frequency detection works with tone generator app
- [ ] Performance charts display data
- [ ] CSV export creates file successfully
- [ ] App works on phone (portrait/landscape)
- [ ] App works on tablet with larger grid

### Testing with Tone Generator

1. Install a tone generator app (e.g., "Tone Generator" by netigen)
2. Generate a 440 Hz sine wave
3. Verify all three implementations detect ~440 Hz
4. Compare processing latencies

## Future Enhancements

- [ ] Full Python FFT integration via Chaquopy
- [ ] Go FFT implementation (via gomobile)
- [ ] Rust FFT implementation via JNI
- [ ] Real-time divergence calculation between implementations
- [ ] Spectrogram view
- [ ] Frequency range selection
- [ ] Audio file playback and analysis
- [ ] Benchmark mode with synthetic signals
- [ ] Play Store deployment automation

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test on physical devices
5. Submit a pull request

## License

MIT License - See main repository LICENSE file

## Credits

- FFT implementations from FlaccidFacade/FFT repository
- MPAndroidChart by PhilJay
- Android AudioRecord API documentation
