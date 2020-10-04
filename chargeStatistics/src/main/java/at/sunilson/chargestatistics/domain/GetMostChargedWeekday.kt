package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.formatPattern
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class GetMostChargedWeekday @Inject constructor() :
    AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            if (params.isEmpty()) return@of null

            val weekday = params
                .filter { it.batteryStatus.isCharging }
                .distinctBy { it.timestamp.toZonedDateTime().toLocalDate() }
                .groupBy { it.timestamp.toZonedDateTime().toLocalDate().dayOfWeek }
                .mapValues { it.value.size }
                .maxByOrNull { it.value }
                ?.key

            Statistic.Fact(
                "mostChargedWeekday",
                "Tag mit meisten Ladungen",
                "${weekday?.formatPattern("EEEE")}"
            )
        }
}