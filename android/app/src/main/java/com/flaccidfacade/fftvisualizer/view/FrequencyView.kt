package com.flaccidfacade.fftvisualizer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.flaccidfacade.fftvisualizer.R
import kotlin.math.log10
import kotlin.math.min

/**
 * Custom view for displaying frequency domain (FFT magnitudes)
 */
class FrequencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var frequencyData: FloatArray = FloatArray(0)
    private var useLogScale = true
    
    private val paint = Paint().apply {
        color = context.getColor(R.color.frequency_color)
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    private val gridPaint = Paint().apply {
        color = context.getColor(R.color.grid_color)
        strokeWidth = 1f
        isAntiAlias = true
    }
    
    fun updateFrequencies(data: FloatArray, logScale: Boolean = true) {
        frequencyData = data
        useLogScale = logScale
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val w = width.toFloat()
        val h = height.toFloat()
        
        // Draw baseline
        canvas.drawLine(0f, h, w, h, gridPaint)
        
        if (frequencyData.isEmpty()) {
            return
        }
        
        // Find max magnitude for scaling
        val maxMag = frequencyData.maxOrNull() ?: 1f
        if (maxMag == 0f) return
        
        // Draw frequency bars
        val barWidth = w / frequencyData.size
        for (i in frequencyData.indices) {
            val magnitude = if (useLogScale && frequencyData[i] > 0) {
                // Use logarithmic scale for better visualization
                val dbValue = 20 * log10(frequencyData[i] / maxMag)
                // Map -60dB to 0dB range to 0 to 1
                ((dbValue + 60) / 60).coerceIn(0f, 1f)
            } else {
                (frequencyData[i] / maxMag).coerceIn(0f, 1f)
            }
            
            val barHeight = magnitude * h
            val x = i * barWidth
            canvas.drawRect(x, h - barHeight, x + barWidth - 1, h, paint)
        }
    }
}
