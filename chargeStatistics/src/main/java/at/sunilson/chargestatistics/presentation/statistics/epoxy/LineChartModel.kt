package at.sunilson.chargestatistics.presentation.statistics.epoxy

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.presentationcore.delegates.ThemeColorDelegate
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@EpoxyModelClass
abstract class LineChartModel : EpoxyModelWithHolder<LineChartModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Statistic.Chart.Line

    override fun bind(holder: Holder) {
        val primaryColor by ThemeColorDelegate(holder.chart.context, R.attr.colorPrimary)

        holder.title.text = data.label
        holder.chart.setDrawBorders(true)
        holder.chart.description.isEnabled = false
        holder.chart.legend.isEnabled = false
        holder.chart.extraLeftOffset = 10f
        holder.chart.extraRightOffset = 10f

        holder.chart.xAxis.apply {
            granularity = data.xGranularity
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(false)
            if (data.xValueFormatter != null) {
                valueFormatter = data.xValueFormatter
            }
        }

        holder.chart.axisLeft.apply { isEnabled = false }

        holder.chart.axisRight.apply {
            isEnabled = true
            granularity = data.yGranularity
            if (data.yValueFormatter != null) {
                valueFormatter = data.yValueFormatter
            }
        }

        holder.chart.data = LineData(
            LineDataSet(data.entries, data.label).apply {
                cubicIntensity = 0.2f
                setDrawFilled(true)
                setDrawValues(false)
                setDrawHorizontalHighlightIndicator(false)
                circleRadius = 5f
                circleColors = listOf(primaryColor)
                colors = listOf(primaryColor)
                lineWidth = 2f
            }
        )
        holder.chart.invalidate()
    }

    override fun getDefaultLayout() = R.layout.line_chart_model

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val chart by bind<LineChart>(R.id.line_chart)
    }
}
