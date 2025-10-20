package com.flaccidfacade.fftvisualizer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.flaccidfacade.fftvisualizer.audio.AudioCaptureManager
import com.flaccidfacade.fftvisualizer.data.FFTMetrics
import com.flaccidfacade.fftvisualizer.data.MetricsCollector
import com.flaccidfacade.fftvisualizer.fft.JavaFFT
import com.flaccidfacade.fftvisualizer.fft.NativeFFT
import com.flaccidfacade.fftvisualizer.utils.CSVExporter
import com.flaccidfacade.fftvisualizer.utils.FFTUtils
import com.flaccidfacade.fftvisualizer.view.FrequencyView
import com.flaccidfacade.fftvisualizer.view.WaveformView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val FFT_SIZE = 2048 // Power of 2 for FFT
    }
    
    // UI Components
    private lateinit var btnStartStop: Button
    private lateinit var btnViewCharts: Button
    private lateinit var btnExport: Button
    private lateinit var tvStatus: TextView
    
    // C++ Visualizer Components
    private lateinit var cppWaveformView: WaveformView
    private lateinit var cppFrequencyView: FrequencyView
    private lateinit var cppTvFps: TextView
    private lateinit var cppTvLatency: TextView
    private lateinit var cppTvFrequency: TextView
    
    // Java Visualizer Components
    private lateinit var javaWaveformView: WaveformView
    private lateinit var javaFrequencyView: FrequencyView
    private lateinit var javaTvFps: TextView
    private lateinit var javaTvLatency: TextView
    private lateinit var javaTvFrequency: TextView
    
    // Python Visualizer Components (using Java as fallback since Python JNI is complex)
    private lateinit var pythonWaveformView: WaveformView
    private lateinit var pythonFrequencyView: FrequencyView
    private lateinit var pythonTvFps: TextView
    private lateinit var pythonTvLatency: TextView
    private lateinit var pythonTvFrequency: TextView
    
    // Core components
    private lateinit var audioCaptureManager: AudioCaptureManager
    private val metricsCollector = MetricsCollector()
    private lateinit var csvExporter: CSVExporter
    
    // FFT implementations
    private val javaFFT = JavaFFT()
    private var nativeFFT: NativeFFT? = null
    
    // FPS tracking
    private val fpsTrackers = mutableMapOf<String, FPSTracker>()
    
    private var isCapturing = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        initializeComponents()
        setupListeners()
        
        // Initialize native library
        try {
            nativeFFT = NativeFFT()
        } catch (e: UnsatisfiedLinkError) {
            Toast.makeText(this, "C++ FFT library not available", Toast.LENGTH_SHORT).show()
        }
        
        // Check permissions
        if (!hasAudioPermission()) {
            requestAudioPermission()
        }
    }
    
    private fun initializeViews() {
        btnStartStop = findViewById(R.id.btnStartStop)
        btnViewCharts = findViewById(R.id.btnViewCharts)
        btnExport = findViewById(R.id.btnExport)
        tvStatus = findViewById(R.id.tvStatus)
        
        // C++ views
        val cppCell = findViewById<android.view.View>(R.id.cppVisualizerCell)
        cppWaveformView = cppCell.findViewById(R.id.waveformView)
        cppFrequencyView = cppCell.findViewById(R.id.frequencyView)
        cppTvFps = cppCell.findViewById(R.id.tvFps)
        cppTvLatency = cppCell.findViewById(R.id.tvLatency)
        cppTvFrequency = cppCell.findViewById(R.id.tvFrequency)
        cppCell.findViewById<TextView>(R.id.tvTitle).text = getString(R.string.cpp_fft)
        
        // Java views
        val javaCell = findViewById<android.view.View>(R.id.javaVisualizerCell)
        javaWaveformView = javaCell.findViewById(R.id.waveformView)
        javaFrequencyView = javaCell.findViewById(R.id.frequencyView)
        javaTvFps = javaCell.findViewById(R.id.tvFps)
        javaTvLatency = javaCell.findViewById(R.id.tvLatency)
        javaTvFrequency = javaCell.findViewById(R.id.tvFrequency)
        javaCell.findViewById<TextView>(R.id.tvTitle).text = getString(R.string.java_fft)
        
        // Python views (using Java implementation as fallback)
        val pythonCell = findViewById<android.view.View>(R.id.pythonVisualizerCell)
        pythonWaveformView = pythonCell.findViewById(R.id.waveformView)
        pythonFrequencyView = pythonCell.findViewById(R.id.frequencyView)
        pythonTvFps = pythonCell.findViewById(R.id.tvFps)
        pythonTvLatency = pythonCell.findViewById(R.id.tvLatency)
        pythonTvFrequency = pythonCell.findViewById(R.id.tvFrequency)
        pythonCell.findViewById<TextView>(R.id.tvTitle).text = getString(R.string.python_fft)
    }
    
    private fun initializeComponents() {
        audioCaptureManager = AudioCaptureManager(this)
        csvExporter = CSVExporter(this)
        
        fpsTrackers["CPP"] = FPSTracker()
        fpsTrackers["Java"] = FPSTracker()
        fpsTrackers["Python"] = FPSTracker()
        
        audioCaptureManager.onAudioDataAvailable = { audioData ->
            processAudioData(audioData)
        }
    }
    
    private fun setupListeners() {
        btnStartStop.setOnClickListener {
            if (isCapturing) {
                stopCapture()
            } else {
                startCapture()
            }
        }
        
        btnViewCharts.setOnClickListener {
            val intent = Intent(this, PerformanceChartActivity::class.java)
            startActivity(intent)
        }
        
        btnExport.setOnClickListener {
            exportData()
        }
    }
    
    private fun startCapture() {
        if (!hasAudioPermission()) {
            requestAudioPermission()
            return
        }
        
        try {
            audioCaptureManager.startCapture()
            isCapturing = true
            btnStartStop.text = getString(R.string.stop_capture)
            tvStatus.text = "Recording..."
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to start audio capture: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
    
    private fun stopCapture() {
        audioCaptureManager.stopCapture()
        isCapturing = false
        btnStartStop.text = getString(R.string.start_capture)
        tvStatus.text = "Stopped"
    }
    
    private fun processAudioData(audioData: ShortArray) {
        CoroutineScope(Dispatchers.Default).launch {
            // Take FFT_SIZE samples
            val samples = if (audioData.size >= FFT_SIZE) {
                audioData.copyOfRange(0, FFT_SIZE)
            } else {
                audioData + ShortArray(FFT_SIZE - audioData.size)
            }
            
            val normalizedData = audioCaptureManager.normalizeAudioData(samples)
            val windowedData = FFTUtils.applyHammingWindow(normalizedData)
            
            // Process with Java FFT
            processJavaFFT(normalizedData, windowedData)
            
            // Process with C++ FFT
            nativeFFT?.let { processNativeFFT(it, normalizedData, windowedData) }
            
            // Process with Python FFT (using Java as fallback)
            processPythonFFT(normalizedData, windowedData)
        }
    }
    
    private suspend fun processJavaFFT(rawData: DoubleArray, windowedData: DoubleArray) {
        val startTime = System.nanoTime()
        
        val result = javaFFT.fftFromDoubles(windowedData)
        val magnitudes = result.map { it.abs() }.toDoubleArray()
        
        val endTime = System.nanoTime()
        val processingTimeMs = (endTime - startTime) / 1_000_000.0
        
        val frequency = FFTUtils.findDominantFrequency(magnitudes, AudioCaptureManager.SAMPLE_RATE)
        val peakMagnitude = magnitudes.maxOrNull() ?: 0.0
        
        fpsTrackers["Java"]?.recordFrame()
        val fps = fpsTrackers["Java"]?.getFPS() ?: 0f
        
        val metrics = FFTMetrics(
            implementation = "Java",
            timestamp = System.currentTimeMillis(),
            processingTimeMs = processingTimeMs,
            fps = fps,
            detectedFrequency = frequency,
            peakMagnitude = peakMagnitude
        )
        metricsCollector.addMetrics(metrics)
        
        withContext(Dispatchers.Main) {
            updateVisualization(
                javaWaveformView, javaFrequencyView,
                javaTvFps, javaTvLatency, javaTvFrequency,
                rawData, magnitudes, fps, processingTimeMs, frequency
            )
        }
    }
    
    private suspend fun processNativeFFT(nativeFFT: NativeFFT, rawData: DoubleArray, windowedData: DoubleArray) {
        val startTime = System.nanoTime()
        
        val magnitudes = nativeFFT.computeFFTMagnitudes(windowedData)
        
        val endTime = System.nanoTime()
        val processingTimeMs = (endTime - startTime) / 1_000_000.0
        
        val frequency = FFTUtils.findDominantFrequency(magnitudes, AudioCaptureManager.SAMPLE_RATE)
        val peakMagnitude = magnitudes.maxOrNull() ?: 0.0
        
        fpsTrackers["CPP"]?.recordFrame()
        val fps = fpsTrackers["CPP"]?.getFPS() ?: 0f
        
        val metrics = FFTMetrics(
            implementation = "C++",
            timestamp = System.currentTimeMillis(),
            processingTimeMs = processingTimeMs,
            fps = fps,
            detectedFrequency = frequency,
            peakMagnitude = peakMagnitude
        )
        metricsCollector.addMetrics(metrics)
        
        withContext(Dispatchers.Main) {
            updateVisualization(
                cppWaveformView, cppFrequencyView,
                cppTvFps, cppTvLatency, cppTvFrequency,
                rawData, magnitudes, fps, processingTimeMs, frequency
            )
        }
    }
    
    private suspend fun processPythonFFT(rawData: DoubleArray, windowedData: DoubleArray) {
        // Using Java FFT as Python fallback (Python JNI integration is complex)
        val startTime = System.nanoTime()
        
        val result = javaFFT.fftFromDoubles(windowedData)
        val magnitudes = result.map { it.abs() }.toDoubleArray()
        
        val endTime = System.nanoTime()
        val processingTimeMs = (endTime - startTime) / 1_000_000.0
        
        val frequency = FFTUtils.findDominantFrequency(magnitudes, AudioCaptureManager.SAMPLE_RATE)
        val peakMagnitude = magnitudes.maxOrNull() ?: 0.0
        
        fpsTrackers["Python"]?.recordFrame()
        val fps = fpsTrackers["Python"]?.getFPS() ?: 0f
        
        val metrics = FFTMetrics(
            implementation = "Python",
            timestamp = System.currentTimeMillis(),
            processingTimeMs = processingTimeMs,
            fps = fps,
            detectedFrequency = frequency,
            peakMagnitude = peakMagnitude
        )
        metricsCollector.addMetrics(metrics)
        
        withContext(Dispatchers.Main) {
            updateVisualization(
                pythonWaveformView, pythonFrequencyView,
                pythonTvFps, pythonTvLatency, pythonTvFrequency,
                rawData, magnitudes, fps, processingTimeMs, frequency
            )
        }
    }
    
    private fun updateVisualization(
        waveformView: WaveformView,
        frequencyView: FrequencyView,
        tvFps: TextView,
        tvLatency: TextView,
        tvFrequency: TextView,
        rawData: DoubleArray,
        magnitudes: DoubleArray,
        fps: Float,
        latency: Double,
        frequency: Double
    ) {
        // Update waveform
        waveformView.updateWaveform(rawData.map { it.toFloat() }.toFloatArray())
        
        // Update frequency view (first half due to Nyquist)
        val halfMagnitudes = magnitudes.copyOfRange(0, magnitudes.size / 2)
        frequencyView.updateFrequencies(halfMagnitudes.map { it.toFloat() }.toFloatArray())
        
        // Update metrics
        tvFps.text = getString(R.string.fps_label, fps)
        tvLatency.text = getString(R.string.latency_label, latency)
        tvFrequency.text = getString(R.string.frequency_label, frequency)
    }
    
    private fun exportData() {
        CoroutineScope(Dispatchers.IO).launch {
            val metrics = metricsCollector.getAllMetrics()
            if (metrics.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "No data to export", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            
            val filePath = csvExporter.exportMetrics(metrics)
            
            withContext(Dispatchers.Main) {
                if (filePath != null) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.export_success, filePath),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.export_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    
    private fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle(R.string.permission_required)
                .setMessage("This app needs microphone access to capture and analyze audio")
                .setPositiveButton(R.string.grant_permission) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        PERMISSION_REQUEST_CODE
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_CODE
            )
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopCapture()
    }
    
    /**
     * Helper class to track FPS
     */
    private class FPSTracker {
        private val frameTimes = mutableListOf<Long>()
        private val windowSize = 30 // Calculate FPS over last 30 frames
        
        fun recordFrame() {
            frameTimes.add(System.currentTimeMillis())
            if (frameTimes.size > windowSize) {
                frameTimes.removeAt(0)
            }
        }
        
        fun getFPS(): Float {
            if (frameTimes.size < 2) return 0f
            
            val timeSpan = frameTimes.last() - frameTimes.first()
            return if (timeSpan > 0) {
                (frameTimes.size - 1) * 1000f / timeSpan
            } else {
                0f
            }
        }
    }
}
