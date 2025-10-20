package com.flaccidfacade.fftvisualizer.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import kotlin.math.abs

/**
 * Audio capture manager using AudioRecord for low-latency microphone input
 */
class AudioCaptureManager(private val context: Context) {
    
    companion object {
        const val SAMPLE_RATE = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val BUFFER_SIZE_FACTOR = 4
    }
    
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var captureJob: Job? = null
    
    private val bufferSize = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT
    ) * BUFFER_SIZE_FACTOR
    
    var onAudioDataAvailable: ((ShortArray) -> Unit)? = null
    
    fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun startCapture() {
        if (!hasPermission()) {
            throw SecurityException("Audio recording permission not granted")
        }
        
        if (isRecording) return
        
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize
            )
            
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                throw IllegalStateException("AudioRecord initialization failed")
            }
            
            audioRecord?.startRecording()
            isRecording = true
            
            captureJob = CoroutineScope(Dispatchers.IO).launch {
                val buffer = ShortArray(bufferSize / 2) // 16-bit samples
                
                while (isRecording && isActive) {
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    
                    if (readSize > 0) {
                        // Process audio data on callback
                        withContext(Dispatchers.Main) {
                            onAudioDataAvailable?.invoke(buffer.copyOf(readSize))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopCapture()
        }
    }
    
    fun stopCapture() {
        isRecording = false
        captureJob?.cancel()
        captureJob = null
        
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        audioRecord = null
    }
    
    fun isCapturing() = isRecording
    
    /**
     * Convert short array to normalized double array for FFT processing
     */
    fun normalizeAudioData(data: ShortArray): DoubleArray {
        return DoubleArray(data.size) { i ->
            data[i].toDouble() / Short.MAX_VALUE
        }
    }
}
