package com.flaccidfacade.fftvisualizer.utils

import android.content.Context
import android.os.Environment
import com.flaccidfacade.fftvisualizer.data.FFTMetrics
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for exporting metrics data to CSV
 */
class CSVExporter(private val context: Context) {
    
    fun exportMetrics(metrics: List<FFTMetrics>): String? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "fft_metrics_$timestamp.csv"
            
            // Use app-specific directory (doesn't require storage permission)
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(downloadsDir, fileName)
            
            val writer = CSVWriter(FileWriter(file))
            
            // Write header
            writer.writeNext(arrayOf(
                "Timestamp",
                "Implementation",
                "Processing Time (ms)",
                "FPS",
                "Detected Frequency (Hz)",
                "Peak Magnitude",
                "Divergence"
            ))
            
            // Write data
            metrics.forEach { metric ->
                writer.writeNext(arrayOf(
                    metric.timestamp.toString(),
                    metric.implementation,
                    String.format("%.4f", metric.processingTimeMs),
                    String.format("%.2f", metric.fps),
                    String.format("%.2f", metric.detectedFrequency),
                    String.format("%.4f", metric.peakMagnitude),
                    String.format("%.4f", metric.divergenceFromReference)
                ))
            }
            
            writer.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
