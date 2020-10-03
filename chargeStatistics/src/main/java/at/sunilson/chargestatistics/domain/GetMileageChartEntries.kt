package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.LineChartData
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetMileageChartEntries @Inject constructor() :
    FlowUseCase<LineChartData?, List<ChargeTrackingPoint>>() {
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

        val result = LineChartData(
            "MileageToDate",
            86400000f,
            1f,
            "Gefahrene Kilometer",
            entries,
            pointValueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return "${entry?.y?.toInt()} km"
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