package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.database.VehicleDao
import at.sunilson.database.mappers.toDatabaseEntity
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.toEntity
import at.sunilson.vehicle.data.toVehicleList
import com.github.kittinunf.result.coroutines.SuspendableResult
import timber.log.Timber
import javax.inject.Inject

internal class RefreshAllVehicles @Inject constructor(
    private val repository: VehicleRepository,
    private val vehicleService: VehicleService,
    private val vehicleDao: VehicleDao
) : AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        //TODO Filter only for zoes
        val vehicles = vehicleService.getAllVehicles(repository.kamereonAccountID).toVehicleList()
        Timber.d("Got vehicle list: $vehicles")

        Timber.d("Refreshing vehicles battery status...")
        //TODO Parallel
        val enrichedVehicles = vehicles.map { vehicle ->
            val batteryStatus = vehicleService
                .getBatteryStatus(repository.kamereonAccountID, vehicle.vin)
                .toEntity()

            val kilometerReading = vehicleService
                .getKilometerReading(repository.kamereonAccountID, vehicle.vin)
                .toEntity()

            vehicle.copy(batteryStatus = batteryStatus, mileageKm = kilometerReading)
        }

        vehicleDao.upsertVehicles(enrichedVehicles.map { it.toDatabaseEntity() })
    }
}