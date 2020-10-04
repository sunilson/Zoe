package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.coroutines.doParallelWithResult
import com.github.kittinunf.result.coroutines.getOrNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetStatsticsChartEntries @Inject constructor(
    private val getAllChargeTrackingPoints: GetAllChargeTrackingPoints,
    private val getMileageChartEntries: GetMileageChartEntries,
    private val getBatterylevelChartEntries: GetBatterylevelChartEntries,
    private val getMileagePerDayEntries: GetMileagePerDayEntries,
    private val getAverageMileagePerDay: GetAverageMileagePerDay
) : FlowUseCase<List<Statistic>, String>() {
    @ExperimentalCoroutinesApi
    override fun run(params: String) = getAllChargeTrackingPoints(params)
        .map { chargeTrackingPoints ->
            doParallelWithResult(
                { getMileageChartEntries(chargeTrackingPoints).getOrNull() },
                { getBatterylevelChartEntries(chargeTrackingPoints).getOrNull() },
                { getMileagePerDayEntries(chargeTrackingPoints).getOrNull() },
                { getAverageMileagePerDay(chargeTrackingPoints).getOrNull() }
            )
        }
        .map { it.filterNotNull() }
}