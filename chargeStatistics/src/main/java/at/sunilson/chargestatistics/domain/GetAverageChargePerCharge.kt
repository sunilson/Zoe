package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule.GERMAN_FORMAT
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

internal class GetAverageChargePerCharge @Inject constructor(
    private val extractChargingProcedures: ExtractChargingProcedures,
    @Named(GERMAN_FORMAT) private val formatter: NumberFormat
) : AsyncUseCase<Statistic.Fact, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact, Exception> {
            val chargingProcedures = extractChargingProcedures(params).get()
            val count = chargingProcedures.size
            val sum = chargingProcedures.sumBy { it.energyLevelDifference }

            Statistic.Fact(
                "averageChargePerCharge",
                "Ø kWh per Ladung",
                "${formatter.format(sum / count.toFloat())} kWh"
            )
        }
}