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

internal class GetMileagePerMonthEntries @Inject constructor() :
    AsyncUseCase<Statistic.Chart.Bar?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Chart.Bar?, Exception> {
            var startDate: LocalDate? = null
            val groupedEntries = params.groupBy {
                val date = it.timestamp.toZonedDateTime().toLocalDate()
                "${date.year}.${date.month}"
            }

            var monthIndex = 0f
            val entries = groupedEntries.map { (_, value) ->
                if (startDate == null) {
                    startDate = value.first().timestamp.toZonedDateTime().toLocalDate()
                }

                BarEntry(
                    monthIndex,
                    when {
                        value.isEmpty() -> 0f
                        value.size == 1 -> 0f
                        else -> value.last().mileageKm.toFloat() - value.first().mileageKm.toFloat()
                    }
                ).also { monthIndex++ }
            }

            val result = Statistic.Chart.Bar(
                "MileagePerMonth",
                entries,
                1f,
                1f,
                "Gefahrene Kilometer/Monat",
                yValueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return "${value.toInt()} km"
                    }
                },
                xValueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${startDate?.plusMonths(value.toLong())?.formatPattern("MM.YY")}"
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
