package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import java.time.temporal.ChronoUnit.DAYS
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

            val startDate = params.first().timestamp.toZonedDateTime().toLocalDate()
            val lastDate = params.last().timestamp.toZonedDateTime().toLocalDate()
            val dayCount = DAYS.between(startDate, lastDate) + 1
            val sum = deChargingProcedures.sumBy { it.energyLevelDifference }

            Statistic.Fact(
                "averageEnergyPerDay",
                "Ø Verbrauch/Tag",
                "${formatter.format(sum / dayCount.toFloat())} kWh"
            )
        }
}