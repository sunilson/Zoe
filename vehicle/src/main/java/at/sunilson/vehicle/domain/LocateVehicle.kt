package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.entities.Location
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.toEntity
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class LocateVehicle @Inject constructor(
    private val vehicleService: VehicleService,
    private val repository: VehicleRepository
) : AsyncUseCase<Location, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Location, Exception> {
        vehicleService.getVehicleLocation(repository.kamereonAccountID, params).toEntity()
    }
}