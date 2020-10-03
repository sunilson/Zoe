package at.sunilson.chargestatistics.domain.entities

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BaseEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

sealed class ChartData<T : BaseEntry>() {
    abstract val id: String
    abstract val entries: List<BaseEntry>
    abstract val xGranularity: Float
    abstract val yGranularity: Float
    abstract val label: String
    abstract val xValueFormatter: ValueFormatter?
    abstract val yValueFormatter: ValueFormatter?

    data class Line(
        override val id: String,
        override val entries: List<Entry>,
        override val xGranularity: Float,
        override val yGranularity: Float,
        override val label: String,
        override val xValueFormatter: ValueFormatter? = null,
        override val yValueFormatter: ValueFormatter? = null
    ) : ChartData<Entry>()

    data class Bar(
        override val id: String,
        override val entries: List<BarEntry>,
        override val xGranularity: Float,
        override val yGranularity: Float,
        override val label: String,
        override val xValueFormatter: ValueFormatter? = null,
        override val yValueFormatter: ValueFormatter? = null
    ) : ChartData<BarEntry>()
}