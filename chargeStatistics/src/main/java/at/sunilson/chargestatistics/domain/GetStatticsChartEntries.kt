package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.coroutines.doParallelWithResult
import com.github.kittinunf.result.coroutines.getOrNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Suppress("LongParameterList")
internal class GetStatticsChartEntries @Inject constructor(
    private val getAllChargeTrackingPoints: GetAllChargeTrackingPoints,
    private val getMileageChartEntries: GetMileageChartEntries,
    private val getBatterylevelChartEntries: GetBatterylevelChartEntries,
    private val getMileagePerMonthEntries: GetMileagePerMonthEntries,
    private val getAverageMileagePerDay: GetAverageMileagePerDay,
    private val getMostChargedWeekday: GetMostChargedWeekday,
    private val getAverageChargePerCharge: GetAverageChargePerCharge,
    private val getAverageEnergyPerDay: GetAverageEnergyPerDay,
    private val getMostVisitedLocation: GetMostVisitedLocation,
    private val getEnergyPerMonth: GetEnergyPerMonth,
    private val getAverageMileageTotal: GetAverageMileageTotal
) : FlowUseCase<List<Statistic>, String>() {
    @ExperimentalCoroutinesApi
    override fun run(params: String) = getAllChargeTrackingPoints(params)
        .map { chargeTrackingPoints ->
            doParallelWithResult(
                { getAverageMileageTotal(chargeTrackingPoints).getOrNull() },
                { getMileageChartEntries(chargeTrackingPoints).getOrNull() },
                { getBatterylevelChartEntries(chargeTrackingPoints).getOrNull() },
                { getMileagePerMonthEntries(chargeTrackingPoints).getOrNull() },
                { getAverageMileagePerDay(chargeTrackingPoints).getOrNull() },
                { getAverageEnergyPerDay(chargeTrackingPoints).getOrNull() },
                { getAverageChargePerCharge(chargeTrackingPoints).getOrNull() },
                { getMostChargedWeekday(chargeTrackingPoints).getOrNull() },
                { getMostVisitedLocation(chargeTrackingPoints).getOrNull() },
                { getEnergyPerMonth(chargeTrackingPoints).getOrNull() },
            )
                .filterNotNull()
                .sortedWith { o1, o2 ->
                    when {
                        o1 is Statistic.Fact && o2 !is Statistic.Fact -> -1
                        o1 !is Statistic.Fact && o2 is Statistic.Fact -> 1
                        else -> 0
                    }
                }
        }
}
