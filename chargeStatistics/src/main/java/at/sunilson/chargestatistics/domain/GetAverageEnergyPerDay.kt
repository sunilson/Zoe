package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

internal class GetAverageEnergyPerDay @Inject constructor(
    private val extractDeChargingProcedures: ExtractDeChargingProcedures,
    @Named(NumberFormatModule.GERMAN_FORMAT) private val formatter: NumberFormat
) : AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            val deChargingProcedures = extractDeChargingProcedures(params).get()
            if (deChargingProcedures.isEmpty()) return@of null
            val count = deChargingProcedures.size
            val sum = deChargingProcedures.sumBy { it.energyLevelDifference }

            Statistic.Fact(
                "averageEnergyPerDay",
                "Ø kWh Verbrauch/Tag",
                "${formatter.format(sum / count.toFloat())} kWh"
            )
        }
}