package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChartData
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetStatsticsChartEntries @Inject constructor(
    private val getAllChargeTrackingPoints: GetAllChargeTrackingPoints,
    private val getMileageChartEntries: GetMileageChartEntries,
    private val getBatterylevelChartEntries: GetBatterylevelChartEntries,
    private val getMileagePerDayEntries: GetMileagePerDayEntries
) : FlowUseCase<List<ChartData<*>>, String>() {
    @ExperimentalCoroutinesApi
    override fun run(params: String) = getAllChargeTrackingPoints(params)
        .flatMapLatest { chargeTrackingPoints ->
            combine(
                listOf(
                    getMileageChartEntries(chargeTrackingPoints),
                    getBatterylevelChartEntries(chargeTrackingPoints),
                    getMileagePerDayEntries(chargeTrackingPoints)
                )
            ) { entriesList -> entriesList.toList() }
        }
        .map { it.filterNotNull() }
}