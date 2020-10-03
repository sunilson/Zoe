package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChartData
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.datetime.formatPattern
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

internal class GetMileagePerDayEntries @Inject constructor() :
    FlowUseCase<ChartData.Bar?, List<ChargeTrackingPoint>>() {
    override fun run(params: List<ChargeTrackingPoint>) = flow {

        var startDate: LocalDate? = null
        val groupedEntries = params.groupBy { it.timestamp.toZonedDateTime().toLocalDate() }

        var dayIndex = 0
        val entries = groupedEntries.map { (_, value) ->
            val day = value.first().timestamp.toZonedDateTime()
            if (startDate == null) startDate = day.toLocalDate()
            BarEntry(
                dayIndex.toFloat(),
                when {
                    value.isEmpty() -> 0f
                    value.size == 1 -> 0f
                    else -> value.last().mileageKm.toFloat() - value.first().mileageKm.toFloat()
                }
            ).also { dayIndex++ }
        }

        val result = ChartData.Bar(
            "MileagePerDay",
            entries,
            1f,
            1f,
            "Gefahrene Kilometer pro Tag",
            yValueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return "${value.toInt()} km"
                }
            },
            xValueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${startDate?.plusDays(value.toLong())?.formatPattern("dd.MM.YY")}"
                }
            }
        )

        if (result.entries.size > 1) {
            emit(result)
        } else {
            emit(null)
        }
    }
}