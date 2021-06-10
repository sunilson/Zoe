package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

internal class GetAverageMileageTotal @Inject constructor(
    @Named(NumberFormatModule.GERMAN_FORMAT) private val formatter: NumberFormat,
    private val extractDeChargingProcedures: ExtractDeChargingProcedures
) : AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {

    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            val chargeProcedures = extractDeChargingProcedures(params).get()
            if (chargeProcedures.isEmpty()) return@of null

            val sumKm = chargeProcedures.sumBy { it.kmDifference }
            val sumEnergy = chargeProcedures.sumBy { it.energyLevelDifference }

            val kmRatio = 100.0 / sumKm
            val averageMileage = sumEnergy * kmRatio

            Statistic.Fact(
                "averageMileageTotal",
                "kWh/100km",
                "${formatter.format(averageMileage)} kWh/100km"
            )
        }
}
