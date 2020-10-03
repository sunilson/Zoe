package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChartData
import at.sunilson.chargestatistics.domain.formatters.DateAxisFormatter
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetMileageChartEntries @Inject constructor() :
    FlowUseCase<ChartData.Line?, List<ChargeTrackingPoint>>() {
    override fun run(params: List<ChargeTrackingPoint>) = flow {
        val entries = params.mapIndexedNotNull { index, chargeTrackingPoint ->
            if (index != 0) {
                val prev = params[index - 1]
                if (prev.mileageKm == chargeTrackingPoint.mileageKm) {
                    return@mapIndexedNotNull null
                }
            }

            Entry(
                chargeTrackingPoint.timestamp.toFloat(),
                chargeTrackingPoint.mileageKm.toFloat()
            )
        }

        val result = ChartData.Line(
            "MileageToDate",
            entries,
            86400000f,
            1f,
            "Gefahrene Kilometer",
            yValueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return "${value.toInt()} km"
                }
            },
            xValueFormatter = DateAxisFormatter()
        )

        if (result.entries.size > 1) {
            emit(result)
        } else {
            emit(null)
        }
    }
}