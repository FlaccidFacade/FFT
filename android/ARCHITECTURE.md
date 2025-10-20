# Android FFT Visualizer - Architecture Documentation

## Overview

The Android FFT Visualizer is designed with a modular architecture that separates concerns into distinct layers: UI, Data Processing, Audio Capture, and Native Integration.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                         UI Layer                            │
│  ┌────────────────┐  ┌──────────────────────────────────┐  │
│  │  MainActivity  │  │  PerformanceChartActivity        │  │
│  │                │  │                                   │  │
│  │  - Grid Layout │  │  - Speed Chart                   │  │
│  │  - Controls    │  │  - Accuracy Chart                │  │
│  │  - 3x FFT Viz  │  │  - Divergence Chart              │  │
│  └────────┬───────┘  └──────────────┬───────────────────┘  │
└───────────┼──────────────────────────┼──────────────────────┘
            │                          │
┌───────────▼──────────────────────────▼──────────────────────┐
│                    Presentation Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ WaveformView │  │FrequencyView │  │ FPSTracker       │  │
│  │              │  │              │  │                  │  │
│  │ - Canvas     │  │ - Canvas     │  │ - Frame timing   │  │
│  │ - Time domain│  │ - Freq domain│  │ - FPS calc       │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
            │                          │
┌───────────▼──────────────────────────▼──────────────────────┐
│                   Business Logic Layer                       │
│  ┌─────────────────────┐  ┌──────────────────────────────┐ │
│  │ AudioCaptureManager │  │  FFT Implementations         │ │
│  │                     │  │  ┌────────┐  ┌────────┐      │ │
│  │ - AudioRecord       │  │  │JavaFFT │  │NativeFFT│      │ │
│  │ - Buffer mgmt       │  │  │(Kotlin)│  │(C++ JNI)│      │ │
│  │ - Normalization     │  │  └────────┘  └────────┘      │ │
│  └──────────┬──────────┘  └───────────────┬──────────────┘ │
└─────────────┼──────────────────────────────┼────────────────┘
              │                              │
┌─────────────▼──────────────────────────────▼────────────────┐
│                      Data Layer                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌────────────┐  │
│  │ MetricsCollector│  │   FFTMetrics    │  │ FFTUtils   │  │
│  │                 │  │                 │  │            │  │
│  │ - Time series   │  │ - Data class    │  │ - Window   │  │
│  │ - Aggregation   │  │ - Timestamp     │  │ - Freq det │  │
│  │ - Storage       │  │ - Latency       │  │ - Diverg   │  │
│  └────────┬────────┘  └─────────────────┘  └────────────┘  │
└───────────┼─────────────────────────────────────────────────┘
            │
┌───────────▼─────────────────────────────────────────────────┐
│                   Persistence Layer                          │
│  ┌─────────────────┐                                         │
│  │   CSVExporter   │                                         │
│  │                 │                                         │
│  │ - Write CSV     │                                         │
│  │ - File mgmt     │                                         │
│  └─────────────────┘                                         │
└─────────────────────────────────────────────────────────────┘
            │
┌───────────▼─────────────────────────────────────────────────┐
│                    Native Layer (JNI)                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  fft_jni.cpp                                         │   │
│  │  - JNI bridge                                        │   │
│  │  - Array conversion                                  │   │
│  │  - Calls ../../cpp/fft_impl.cpp                     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### 1. UI Layer

#### MainActivity
- **Purpose**: Main entry point, handles user interactions and orchestrates FFT processing
- **Responsibilities**:
  - Manage audio capture lifecycle (start/stop)
  - Display 3-column grid with FFT visualizations
  - Handle permissions
  - Route to performance charts
  - Trigger CSV export
- **Key Methods**:
  - `startCapture()`: Initializes audio recording
  - `stopCapture()`: Stops audio recording
  - `processAudioData()`: Routes audio to FFT implementations
  - `updateVisualization()`: Updates UI with FFT results

#### PerformanceChartActivity
- **Purpose**: Display historical performance metrics
- **Responsibilities**:
  - Render speed, accuracy, and divergence charts
  - Handle chart interactions (zoom, pan)
  - Load data from MetricsCollector
- **Libraries Used**: MPAndroidChart

### 2. Presentation Layer

#### WaveformView
- **Purpose**: Custom view for time-domain visualization
- **Features**:
  - Canvas-based rendering
  - Normalized waveform display
  - Grid lines for reference
  - Auto-scaling
- **Performance**: Optimized for 60 FPS rendering

#### FrequencyView
- **Purpose**: Custom view for frequency-domain visualization
- **Features**:
  - Bar chart representation
  - Logarithmic scale option (dB)
  - Color-coded magnitude
  - Nyquist frequency handling
- **Performance**: Efficient bar rendering

### 3. Business Logic Layer

#### AudioCaptureManager
- **Purpose**: Manages microphone audio capture
- **Technology**: Android AudioRecord API
- **Configuration**:
  - Sample Rate: 44,100 Hz
  - Format: PCM 16-bit
  - Channel: Mono
  - Buffer: 4x minimum size
- **Flow**:
  1. Initialize AudioRecord
  2. Start recording on background thread (Coroutine)
  3. Read audio data in chunks
  4. Invoke callback with audio buffer
  5. Continue until stopped

#### FFT Implementations

##### JavaFFT (Kotlin)
- **Algorithm**: Cooley-Tukey FFT
- **Implementation**: Pure Kotlin/Java
- **Complexity**: O(N log N)
- **Features**:
  - Power-of-2 padding
  - Recursive divide-and-conquer
  - Complex number arithmetic
- **Performance**: ~2-3ms for 2048 samples

##### NativeFFT (C++ via JNI)
- **Algorithm**: Cooley-Tukey FFT
- **Implementation**: C++11 with STL
- **JNI Bridge**: `fft_jni.cpp`
- **Features**:
  - Optimized native code
  - Direct memory access
  - Standard library only
- **Performance**: ~1ms for 2048 samples

##### Python FFT (Fallback)
- **Current**: Uses JavaFFT as placeholder
- **Future**: Could integrate via Chaquopy or custom CPython embedding
- **Reason for Fallback**: Python JNI integration is complex and adds significant overhead

### 4. Data Layer

#### FFTMetrics
- **Type**: Data class
- **Fields**:
  - `implementation`: String (C++, Java, Python)
  - `timestamp`: Long (milliseconds)
  - `processingTimeMs`: Double
  - `fps`: Float
  - `detectedFrequency`: Double
  - `peakMagnitude`: Double
  - `divergenceFromReference`: Double

#### MetricsCollector
- **Purpose**: Collect and aggregate metrics over time
- **Features**:
  - Thread-safe collection
  - Limited history (last 1000 entries)
  - Per-implementation filtering
  - Statistical aggregation (average, etc.)

#### FFTUtils
- **Purpose**: Utility functions for FFT analysis
- **Functions**:
  - `findDominantFrequency()`: Peak detection
  - `calculateRMS()`: Signal strength
  - `calculateDivergence()`: MSE between implementations
  - `computeMagnitudes()`: Complex to magnitude conversion
  - `applyHammingWindow()`: Windowing function

### 5. Persistence Layer

#### CSVExporter
- **Purpose**: Export metrics to CSV files
- **Library**: OpenCSV
- **Output Location**: App-specific external storage
- **Format**:
  ```
  Timestamp,Implementation,Processing Time (ms),FPS,Detected Frequency (Hz),Peak Magnitude,Divergence
  ```
- **Features**:
  - Timestamped filenames
  - No special permissions required (Android 14+)
  - Compatible with Excel and data analysis tools

### 6. Native Layer

#### fft_jni.cpp
- **Purpose**: JNI bridge between Kotlin and C++
- **Functions**:
  - `Java_com_flaccidfacade_fftvisualizer_fft_NativeFFT_computeFFT`
- **Process**:
  1. Receive Java double array
  2. Convert to C++ std::vector<std::complex<double>>
  3. Call FFT from repository's cpp/fft_impl.cpp
  4. Convert result back to Java double array (interleaved real/imag)
  5. Return to Kotlin

#### CMakeLists.txt
- **Purpose**: CMake build configuration
- **Dependencies**:
  - Repository's C++ FFT implementation
  - Android NDK libraries (log)
- **Build Flags**: -O3 optimization, C++11 standard

## Data Flow

### Audio Processing Pipeline

```
Microphone
    ↓
AudioRecord (44.1kHz, 16-bit PCM)
    ↓
AudioCaptureManager (buffer: 8192 samples)
    ↓
Normalization (Short → Double, [-1, 1])
    ↓
Hamming Window (reduce spectral leakage)
    ↓
┌──────────────┬──────────────┬──────────────┐
│   JavaFFT    │  NativeFFT   │  PythonFFT   │
│   (Kotlin)   │  (C++ JNI)   │  (Java FB)   │
└──────┬───────┴──────┬───────┴──────┬───────┘
       │              │              │
    Magnitude      Magnitude      Magnitude
    Calculation    Calculation    Calculation
       │              │              │
       ├──────────────┼──────────────┤
       ↓              ↓              ↓
   Find Dominant Frequency (peak detection)
       ↓              ↓              ↓
   Record Metrics (latency, FPS, freq)
       ↓              ↓              ↓
   Update Visualization (waveform + frequency)
       ↓              ↓              ↓
   Display in Grid Cell (with overlays)
```

### Metrics Collection Flow

```
FFT Processing
    ↓
Create FFTMetrics object
    ↓
MetricsCollector.addMetrics()
    ↓
Store in time-series buffer (max 1000)
    ↓
Available for:
  ├─→ Performance Charts (live update)
  └─→ CSV Export (batch write)
```

## Threading Model

### Main Thread (UI Thread)
- UI rendering
- User interactions
- View updates (WaveformView, FrequencyView)

### Background Threads (Coroutines)
- Audio capture (Dispatchers.IO)
- FFT processing (Dispatchers.Default)
- Metrics collection (Dispatchers.Default)
- CSV export (Dispatchers.IO)

### Native Thread
- C++ FFT computation (called from Kotlin coroutine)

## Performance Considerations

### Optimization Strategies

1. **Efficient Rendering**
   - Custom views use Canvas (hardware-accelerated)
   - Minimize allocations in onDraw()
   - Only redraw when data changes

2. **Native Code**
   - C++ FFT compiled with -O3
   - Direct memory access
   - Minimal JNI overhead

3. **Coroutines**
   - Non-blocking audio processing
   - Parallel FFT computation possible
   - Structured concurrency

4. **Memory Management**
   - Reuse audio buffers
   - Limited metrics history
   - Efficient array operations

### Benchmarks (Expected on Samsung Galaxy S25 Ultra)

| Implementation | Processing Time | FPS      | Memory   |
|---------------|----------------|----------|----------|
| C++ (Native)  | ~1.0 ms        | 60 FPS   | Low      |
| Java (Kotlin) | ~2.5 ms        | 60 FPS   | Medium   |
| Python (FB)   | ~2.5 ms        | 60 FPS   | Medium   |

## Security Considerations

1. **Permissions**
   - RECORD_AUDIO: Required, runtime permission
   - WRITE_EXTERNAL_STORAGE: Not required (app-specific storage)

2. **Data Privacy**
   - Audio data processed in-memory only
   - No network transmission
   - CSV exports stored locally
   - No cloud backup by default

3. **Input Validation**
   - Audio buffer size checks
   - FFT input validation
   - Safe array indexing

## Testing Strategy

### Unit Tests
- FFT correctness (compare against known values)
- Frequency detection accuracy
- Divergence calculation
- Metrics aggregation

### Integration Tests
- Audio capture → FFT pipeline
- Metrics collection → CSV export
- UI updates from background threads

### UI Tests (Espresso)
- Permission flow
- Start/stop capture
- Navigation to charts
- Export functionality

### Manual Testing
- Real device testing on Samsung devices
- Tone generator validation
- Performance profiling
- Memory leak detection

## Future Enhancements

1. **Additional FFT Implementations**
   - Python (via Chaquopy)
   - Go (via gomobile)
   - Rust (via JNI)

2. **Advanced Visualizations**
   - Spectrogram (waterfall view)
   - 3D frequency plot
   - Phase information

3. **Real-time Analysis**
   - Note detection (musical)
   - Pitch tracking
   - Harmonic analysis

4. **Performance Improvements**
   - GPU-accelerated FFT (RenderScript/Vulkan)
   - Multi-threaded processing
   - SIMD optimizations

5. **User Features**
   - Adjustable FFT size
   - Frequency range zoom
   - Audio file analysis
   - Benchmark mode

## References

- [Android Audio Latency](https://developer.android.com/ndk/guides/audio/audio-latency)
- [JNI Tips](https://developer.android.com/training/articles/perf-jni)
- [Canvas and Drawables](https://developer.android.com/guide/topics/graphics/2d-graphics)
- [Cooley-Tukey FFT Algorithm](https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm)
