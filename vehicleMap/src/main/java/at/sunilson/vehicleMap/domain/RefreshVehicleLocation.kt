package at.sunilson.vehicleMap.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import at.sunilson.vehiclecore.domain.entities.Location
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class RefreshVehicleLocation @Inject constructor(
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao
) : AsyncUseCase<Location, String>() {
    override suspend fun run(vin: String) = SuspendableResult.of<Location, Exception> {
        val result = vehicleCoreService
            .getVehicleLocation(vehicleCoreRepository.kamereonAccountID, vin)
            .toEntity()

        val currentVehicle = vehicleDao.getVehicleOnce(vin)

        if (currentVehicle != null) {
            vehicleDao.upsertVehicles(
                listOf(
                    currentVehicle.copy(
                        vehicle = currentVehicle.vehicle.copy(
                            location = result
                        )
                    )
                )
            )
        }

        result
    }
}