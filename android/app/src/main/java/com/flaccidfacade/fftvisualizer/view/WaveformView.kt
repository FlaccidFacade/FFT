package com.flaccidfacade.fftvisualizer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.flaccidfacade.fftvisualizer.R

/**
 * Custom view for displaying waveform (time domain)
 */
class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var waveformData: FloatArray = FloatArray(0)
    
    private val paint = Paint().apply {
        color = context.getColor(R.color.waveform_color)
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    
    private val gridPaint = Paint().apply {
        color = context.getColor(R.color.grid_color)
        strokeWidth = 1f
        isAntiAlias = true
    }
    
    fun updateWaveform(data: FloatArray) {
        waveformData = data
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val w = width.toFloat()
        val h = height.toFloat()
        val centerY = h / 2
        
        // Draw grid
        canvas.drawLine(0f, centerY, w, centerY, gridPaint)
        
        if (waveformData.isEmpty()) {
            return
        }
        
        // Draw waveform
        val step = w / waveformData.size
        for (i in 0 until waveformData.size - 1) {
            val x1 = i * step
            val y1 = centerY - (waveformData[i] * centerY)
            val x2 = (i + 1) * step
            val y2 = centerY - (waveformData[i + 1] * centerY)
            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}
