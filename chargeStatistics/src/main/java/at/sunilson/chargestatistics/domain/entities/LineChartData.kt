package at.sunilson.chargestatistics.domain.entities

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

data class LineChartData(
    val id: String,
    val xGranularity: Float,
    val yGranularity: Float,
    val label: String,
    val entries: List<Entry>,
    val pointValueFormatter: ValueFormatter? = null,
    val xValueFormatter: ValueFormatter? = null
)