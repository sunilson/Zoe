package at.sunilson.chargestatistics.domain.formatters

import at.sunilson.ktx.datetime.formatPattern
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

internal class DateAxisFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return value.toLong().toZonedDateTime().formatPattern("dd.MM.YY")
    }
}