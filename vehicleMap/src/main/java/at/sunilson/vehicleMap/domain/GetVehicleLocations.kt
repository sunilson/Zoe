package at.sunilson.vehicleMap.domain

import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehicleMap.data.MapsService
import at.sunilson.vehiclecore.domain.entities.Location
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetVehicleLocations @Inject constructor(
    private val getAllChargeTrackingPoints: GetAllChargeTrackingPoints,
    private val mapsService: MapsService
) : FlowUseCase<List<Location>, String>() {

    override fun run(params: String): Flow<List<Location>> {
        return getAllChargeTrackingPoints(params).map {
            it.mapNotNull { it.location }
        }
    }
}