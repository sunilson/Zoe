package at.sunilson.vehicle.domain

import at.sunilson.appointments.domain.RefreshAppointments
import at.sunilson.chargetracking.domain.CheckIfTrackerIsRunning
import at.sunilson.chargetracking.domain.CreateChargePointParams
import at.sunilson.chargetracking.domain.CreateVehicleChargePoint
import at.sunilson.contracts.domain.RefreshContracts
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.toVehicleList
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.RefreshVehicleLocation
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

internal class RefreshAllVehicles @Inject constructor(
    private val vehicleService: VehicleService,
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao,
    private val refreshVehicleLocation: RefreshVehicleLocation,
    private val createVehicleChargePoint: CreateVehicleChargePoint,
    private val checkIfTrackerIsRunning: CheckIfTrackerIsRunning,
    private val refreshAppointments: RefreshAppointments,
    private val refreshContracts: RefreshContracts
) : AsyncUseCase<List<Vehicle>, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<List<Vehicle>, Exception> {

        val kamereonId = vehicleCoreRepository.kamereonAccountID

        val previousVehicles = vehicleDao.getAllVehicles().first().map { it.toEntity() }
        val newVehicles = vehicleService.getAllVehicles(kamereonId).toVehicleList()

        Timber.d("Got vehicle list: $newVehicles")

        //TODO Parallel
        Timber.d("Refreshing vehicles battery status...")
        val enrichedVehicles = newVehicles.map { vehicle ->

            val (location, _) = refreshVehicleLocation(vehicle.vin)

            val batteryStatus = vehicleCoreService
                .getBatteryStatus(kamereonId, vehicle.vin)
                .toEntity()

            val kilometerReading = vehicleCoreService
                .getKilometerReading(kamereonId, vehicle.vin)
                .toEntity()

            //TODO Dont do this every time?
            refreshAppointments(vehicle.vin)
            refreshContracts(vehicle.vin)

            val prev = previousVehicles.firstOrNull { it.vin == vehicle.vin }
            val newVehicle = vehicle.copy(
                batteryStatus = batteryStatus,
                mileageKm = kilometerReading,
                lastChangeTimestamp = prev?.lastChangeTimestamp ?: System.currentTimeMillis()
            )

            //Track vehicle state on refresh also (but only if tracker is running)
            if (checkIfTrackerIsRunning(vehicle.vin).get()) {
                createVehicleChargePoint(
                    CreateChargePointParams(
                        vehicle.vin,
                        batteryStatus,
                        kilometerReading,
                        location
                    )
                )
            }

            when (prev) {
                null -> newVehicle.copy(lastChangeTimestamp = System.currentTimeMillis())
                newVehicle -> newVehicle
                else -> newVehicle.copy(lastChangeTimestamp = System.currentTimeMillis())
            }
        }

        vehicleDao.upsertVehicles(enrichedVehicles.map { it.toDatabaseEntity() })

        enrichedVehicles
    }
}