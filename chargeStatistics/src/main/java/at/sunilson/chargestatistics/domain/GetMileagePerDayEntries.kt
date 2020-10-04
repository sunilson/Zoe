package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.formatPattern
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import javax.inject.Inject

internal class GetMileagePerDayEntries @Inject constructor() :
    AsyncUseCase<Statistic.Chart.Bar?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Chart.Bar?, Exception> {
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

            val result = Statistic.Chart.Bar(
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
                result
            } else {
                null
            }
        }
}