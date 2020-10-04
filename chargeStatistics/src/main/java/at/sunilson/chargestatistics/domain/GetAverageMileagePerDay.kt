package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.text.NumberFormat
import java.time.temporal.ChronoUnit.DAYS
import java.util.*
import javax.inject.Inject

internal class GetAverageMileagePerDay @Inject constructor() :
    AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {

    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            if (params.isEmpty()) return@of null

            val startDate = params.first().timestamp.toZonedDateTime().toLocalDate()
            val lastDate = params.last().timestamp.toZonedDateTime().toLocalDate()
            val dayCount = DAYS.between(startDate, lastDate) + 1
            val kmCount = params.last().mileageKm - params.first().mileageKm

            val average = kmCount / dayCount.toFloat()
            val formatter =
                NumberFormat.getNumberInstance(Locale.GERMAN).apply { maximumFractionDigits = 2 }
            Statistic.Fact(
                "averageMileagePerDay",
                "Kilometer pro Tag",
                "${formatter.format(average)} km"
            )
        }
}