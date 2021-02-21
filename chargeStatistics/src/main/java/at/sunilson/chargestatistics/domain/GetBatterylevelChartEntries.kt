package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargestatistics.domain.formatters.DateAxisFormatter
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import javax.inject.Inject

internal class GetBatterylevelChartEntries @Inject constructor() :
    AsyncUseCase<Statistic.Chart.Line?, List<ChargeTrackingPoint>>() {

    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Chart.Line?, Exception> {
            val entries = params.mapIndexedNotNull { index, chargeTrackingPoint ->
                if (index != 0) {
                    val prev = params[index - 1]
                    if (prev.batteryStatus.batteryLevel == chargeTrackingPoint.batteryStatus.batteryLevel) {
                        return@mapIndexedNotNull null
                    }
                }

                Entry(
                    chargeTrackingPoint.timestamp.toFloat(),
                    chargeTrackingPoint.batteryStatus.batteryLevel.toFloat()
                )
            }

            val result = Statistic.Chart.Line(
                "BatteryLevelToDate",
                entries,
                86400000f,
                1f,
                "Batteriestand",
                yValueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return "${value.toInt()} %"
                    }
                },
                xValueFormatter = DateAxisFormatter()
            )

            if (result.entries.size > 1) {
                result
            } else {
                null
            }
        }
}
