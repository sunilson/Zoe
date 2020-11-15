package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class SelectVehicle @Inject constructor(private val vehicleRepository: VehicleCoreRepository) :
    AsyncUseCase<Unit, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        vehicleRepository.setSelectedVehicle(params)
    }
}