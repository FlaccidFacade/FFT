package com.flaccidfacade.fftvisualizer.data

/**
 * Data class for FFT performance metrics
 */
data class FFTMetrics(
    val implementation: String,
    val timestamp: Long,
    val processingTimeMs: Double,
    val fps: Float,
    val detectedFrequency: Double,
    val peakMagnitude: Double,
    val divergenceFromReference: Double = 0.0
)

/**
 * Container for collected metrics over time
 */
class MetricsCollector {
    private val metricsHistory = mutableListOf<FFTMetrics>()
    
    fun addMetrics(metrics: FFTMetrics) {
        synchronized(metricsHistory) {
            metricsHistory.add(metrics)
            // Keep only last 1000 entries to prevent memory issues
            if (metricsHistory.size > 1000) {
                metricsHistory.removeAt(0)
            }
        }
    }
    
    fun getMetrics(implementation: String): List<FFTMetrics> {
        synchronized(metricsHistory) {
            return metricsHistory.filter { it.implementation == implementation }
        }
    }
    
    fun getAllMetrics(): List<FFTMetrics> {
        synchronized(metricsHistory) {
            return metricsHistory.toList()
        }
    }
    
    fun clear() {
        synchronized(metricsHistory) {
            metricsHistory.clear()
        }
    }
    
    fun getAverageLatency(implementation: String): Double {
        val metrics = getMetrics(implementation)
        return if (metrics.isEmpty()) 0.0 else metrics.map { it.processingTimeMs }.average()
    }
    
    fun getAverageFPS(implementation: String): Float {
        val metrics = getMetrics(implementation)
        return if (metrics.isEmpty()) 0f else metrics.map { it.fps }.average().toFloat()
    }
}
