package com.flaccidfacade.fftvisualizer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.flaccidfacade.fftvisualizer.data.FFTMetrics
import com.flaccidfacade.fftvisualizer.data.MetricsCollector

class PerformanceChartActivity : AppCompatActivity() {
    
    private lateinit var speedChart: LineChart
    private lateinit var accuracyChart: LineChart
    private lateinit var divergenceChart: LineChart
    
    // This would be shared via dependency injection or singleton in production
    private val metricsCollector = MetricsCollector()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_chart)
        
        // Setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        initializeCharts()
        loadData()
    }
    
    private fun initializeCharts() {
        speedChart = findViewById(R.id.speedChart)
        accuracyChart = findViewById(R.id.accuracyChart)
        divergenceChart = findViewById(R.id.divergenceChart)
        
        configureChart(speedChart, "Processing Time (ms)")
        configureChart(accuracyChart, "Detected Frequency (Hz)")
        configureChart(divergenceChart, "Divergence (MSE)")
    }
    
    private fun configureChart(chart: LineChart, yAxisLabel: String) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }
            
            axisLeft.apply {
                textColor = Color.WHITE
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }
            
            axisRight.isEnabled = false
            
            legend.apply {
                textColor = Color.WHITE
                form = Legend.LegendForm.LINE
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            }
        }
    }
    
    private fun loadData() {
        val allMetrics = metricsCollector.getAllMetrics()
        
        if (allMetrics.isEmpty()) {
            // Show empty state
            return
        }
        
        // Group metrics by implementation
        val cppMetrics = allMetrics.filter { it.implementation == "C++" }
        val javaMetrics = allMetrics.filter { it.implementation == "Java" }
        val pythonMetrics = allMetrics.filter { it.implementation == "Python" }
        
        // Load speed chart
        loadSpeedChart(cppMetrics, javaMetrics, pythonMetrics)
        
        // Load accuracy chart
        loadAccuracyChart(cppMetrics, javaMetrics, pythonMetrics)
        
        // Load divergence chart
        loadDivergenceChart(cppMetrics, javaMetrics, pythonMetrics)
    }
    
    private fun loadSpeedChart(
        cppMetrics: List<FFTMetrics>,
        javaMetrics: List<FFTMetrics>,
        pythonMetrics: List<FFTMetrics>
    ) {
        val datasets = mutableListOf<LineDataSet>()
        
        // C++ data
        if (cppMetrics.isNotEmpty()) {
            val entries = cppMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.processingTimeMs.toFloat())
            }
            val dataset = LineDataSet(entries, "C++").apply {
                color = Color.RED
                setCircleColor(Color.RED)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Java data
        if (javaMetrics.isNotEmpty()) {
            val entries = javaMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.processingTimeMs.toFloat())
            }
            val dataset = LineDataSet(entries, "Java").apply {
                color = Color.GREEN
                setCircleColor(Color.GREEN)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Python data
        if (pythonMetrics.isNotEmpty()) {
            val entries = pythonMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.processingTimeMs.toFloat())
            }
            val dataset = LineDataSet(entries, "Python").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        speedChart.data = LineData(datasets.toList())
        speedChart.invalidate()
    }
    
    private fun loadAccuracyChart(
        cppMetrics: List<FFTMetrics>,
        javaMetrics: List<FFTMetrics>,
        pythonMetrics: List<FFTMetrics>
    ) {
        val datasets = mutableListOf<LineDataSet>()
        
        // C++ data
        if (cppMetrics.isNotEmpty()) {
            val entries = cppMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.detectedFrequency.toFloat())
            }
            val dataset = LineDataSet(entries, "C++").apply {
                color = Color.RED
                setCircleColor(Color.RED)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Java data
        if (javaMetrics.isNotEmpty()) {
            val entries = javaMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.detectedFrequency.toFloat())
            }
            val dataset = LineDataSet(entries, "Java").apply {
                color = Color.GREEN
                setCircleColor(Color.GREEN)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Python data
        if (pythonMetrics.isNotEmpty()) {
            val entries = pythonMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.detectedFrequency.toFloat())
            }
            val dataset = LineDataSet(entries, "Python").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        accuracyChart.data = LineData(datasets.toList())
        accuracyChart.invalidate()
    }
    
    private fun loadDivergenceChart(
        cppMetrics: List<FFTMetrics>,
        javaMetrics: List<FFTMetrics>,
        pythonMetrics: List<FFTMetrics>
    ) {
        val datasets = mutableListOf<LineDataSet>()
        
        // C++ data
        if (cppMetrics.isNotEmpty()) {
            val entries = cppMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.divergenceFromReference.toFloat())
            }
            val dataset = LineDataSet(entries, "C++").apply {
                color = Color.RED
                setCircleColor(Color.RED)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Java data
        if (javaMetrics.isNotEmpty()) {
            val entries = javaMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.divergenceFromReference.toFloat())
            }
            val dataset = LineDataSet(entries, "Java").apply {
                color = Color.GREEN
                setCircleColor(Color.GREEN)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        // Python data
        if (pythonMetrics.isNotEmpty()) {
            val entries = pythonMetrics.mapIndexed { index, metric ->
                Entry(index.toFloat(), metric.divergenceFromReference.toFloat())
            }
            val dataset = LineDataSet(entries, "Python").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
            datasets.add(dataset)
        }
        
        divergenceChart.data = LineData(datasets.toList())
        divergenceChart.invalidate()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
