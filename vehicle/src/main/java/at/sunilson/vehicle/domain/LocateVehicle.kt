package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.entities.Location
import javax.inject.Inject

internal class LocateVehicle @Inject constructor(private val repository: VehicleRepository) :
    AsyncUseCase<Location, String>() {
    override suspend fun run(params: String) = repository.locateVehicle(params)
}