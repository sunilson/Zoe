package at.sunilson.chargestatistics.domain

import android.location.Geocoder
import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.entities.Location
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.annotation.Nullable
import javax.inject.Inject

internal class GetMostVisitedLocation @Inject constructor(private val geocoder: Geocoder) :
    AsyncUseCase<Statistic.Fact?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Fact?, Exception> {
            if (params.isEmpty()) return@of null

            val location = params
                .mapNotNull { it.location }
                .groupBy { Location(it.lat, it.lng, 0L) }
                .toList()
                .maxByOrNull { it.second.size }
                ?.first
                ?: return@of null

            val address = geocoder
                .getFromLocation(location.lat, location.lng, 1)
                .firstOrNull() ?: return@of null

            Statistic.Fact(
                "mostVisitedLocation",
                "Meist besuchter Ort",
                address.getAddressLine(0)
            )
        }
}