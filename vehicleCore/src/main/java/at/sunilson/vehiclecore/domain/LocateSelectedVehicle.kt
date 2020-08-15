package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.entities.Location
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class LocateSelectedVehicle @Inject constructor(
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Location, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Location, Exception> {
        vehicleCoreService
            .getVehicleLocation(
                vehicleCoreRepository.kamereonAccountID,
                vehicleCoreRepository.selectedVehicle!!
            ).toEntity()
    }
}