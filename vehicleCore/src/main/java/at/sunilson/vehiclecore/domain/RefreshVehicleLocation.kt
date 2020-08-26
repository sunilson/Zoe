package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
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
        vehicleDao.insertVehicleLocation(result.toDatabaseEntity(vin))
        result
    }
}