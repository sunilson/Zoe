package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.toEntity
import at.sunilson.vehicle.data.toVehicleList
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import timber.log.Timber
import javax.inject.Inject

internal class RefreshAllVehicles @Inject constructor(
    private val repository: VehicleCoreRepository,
    private val vehicleService: VehicleService,
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao
) : AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {

        val kamereonId = vehicleCoreRepository.kamereonAccountID

        //TODO Filter only for zoes
        val vehicles = vehicleService.getAllVehicles(kamereonId).toVehicleList()
        Timber.d("Got vehicle list: $vehicles")

        Timber.d("Refreshing vehicles battery status...")
        //TODO Parallel
        val enrichedVehicles = vehicles.map { vehicle ->
            val batteryStatus = vehicleCoreService
                .getBatteryStatus(kamereonId, vehicle.vin)
                .toEntity()

            val kilometerReading = vehicleCoreService
                .getKilometerReading(kamereonId, vehicle.vin)
                .toEntity()

            vehicle.copy(batteryStatus = batteryStatus, mileageKm = kilometerReading)
        }

        vehicleDao.upsertVehicles(enrichedVehicles.map { it.toDatabaseEntity() })
    }
}