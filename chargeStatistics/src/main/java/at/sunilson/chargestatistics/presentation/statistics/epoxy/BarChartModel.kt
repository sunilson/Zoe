package at.sunilson.chargestatistics.presentation.statistics.epoxy

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.presentationcore.delegates.ThemeColorDelegate
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

@EpoxyModelClass
abstract class BarChartModel : EpoxyModelWithHolder<BarChartModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Statistic.Chart.Bar

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

        holder.chart.data = BarData(
            BarDataSet(data.entries, data.label).apply {
                setDrawValues(false)
                colors = listOf(primaryColor)
            }
        ).apply {
            barWidth = 0.9f
        }
        holder.chart.setFitBars(true)
        holder.chart.invalidate()
    }

    override fun getDefaultLayout() = R.layout.bar_chart_model

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val chart by bind<BarChart>(R.id.bar_chart)
    }
}
