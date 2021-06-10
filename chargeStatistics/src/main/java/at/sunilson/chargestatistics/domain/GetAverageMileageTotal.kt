package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Named

internal class GetAverageMileageTotal @Inject constructor(
    @Named(NumberFormatModule.GERMAN_FORMAT) private val formatter: NumberFormat
) : AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {

    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            if (params.isEmpty()) return@of null

            val startDate = params.first().timestamp.toZonedDateTime().toLocalDate()
            val lastDate = params.last().timestamp.toZonedDateTime().toLocalDate()
            val dayCount = ChronoUnit.DAYS.between(startDate, lastDate) + 1
            val kmCount = params.last().mileageKm - params.first().mileageKm

            val average = kmCount / dayCount.toFloat()
            
            Statistic.Fact(
                "averageMileagePerDay",
                "Ø Kilometer/Tag",
                "${formatter.format(average)} km"
            )
        }
}
