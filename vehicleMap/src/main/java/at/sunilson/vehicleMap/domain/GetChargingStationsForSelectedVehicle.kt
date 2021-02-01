package at.sunilson.vehicleMap.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehicleMap.data.MapsService
import at.sunilson.vehicleMap.data.toEntity
import at.sunilson.vehicleMap.domain.entities.ChargingStation
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

internal class GetChargingStationsForSelectedVehicle @Inject constructor(
    private val mapsService: MapsService,
    private val getSelectedVehicle: GetSelectedVehicle
) : AsyncUseCase<List<ChargingStation>, Double>() {

    private val mutex = Mutex()
    private var previousResult: List<ChargingStation>? = null
    private var previousLocation: Location? = null
    private var previousRadius: Double? = null

    override suspend fun run(params: Double) =
        SuspendableResult.of<List<ChargingStation>, Exception> {
            val location = getSelectedVehicle(Unit).first()?.location
                ?: error("No vehicle with a location found")
            val radius = min(max(params, 0.0), MAX_RADIUS)
            val previousRadiusIsBigger = previousRadius != null && previousRadius!! >= radius

            mutex.withLock {
                if (previousResult != null && previousLocation == location && previousRadiusIsBigger) {
                    return@of previousResult!!
                }

                previousLocation = location
                previousRadius = params
            }

            val result = mapsService.getChargingStations(
                location.lat,
                location.lng,
                radius
            ).map { it.toEntity() }.filter { it.location != null && it.type == "charging spot" }

            previousResult = result
            result
        }

    companion object {
        const val MAX_RADIUS = 50.0
    }
}