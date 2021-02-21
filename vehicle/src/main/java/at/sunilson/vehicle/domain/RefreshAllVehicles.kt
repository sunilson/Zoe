package at.sunilson.vehicle.domain

import at.sunilson.appointments.domain.RefreshAppointments
import at.sunilson.chargetracking.domain.CheckIfTrackerIsRunning
import at.sunilson.chargetracking.domain.CreateChargePointParams
import at.sunilson.chargetracking.domain.CreateVehicleChargePoint
import at.sunilson.contracts.domain.RefreshContracts
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.coroutines.doParallel
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.toVehicleList
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongParameterList")
internal class RefreshAllVehicles @Inject constructor(
    private val vehicleService: VehicleService,
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao,
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

        Timber.d("Refreshing vehicles battery status...")
        val enrichedVehicles = newVehicles.map { vehicle ->
            var batteryStatus: Vehicle.BatteryStatus? = null
            var kilometerReading: Int? = null
            var location: Location? = null

            doParallel(
                {
                    batteryStatus = vehicleCoreService
                        .getBatteryStatus(kamereonId, vehicle.vin)
                        .toEntity()
                },
                {
                    kilometerReading = vehicleCoreService
                        .getKilometerReading(kamereonId, vehicle.vin)
                        .toEntity()
                },
                {
                    location = try {
                        vehicleCoreService.getVehicleLocation(kamereonId, vehicle.vin).toEntity()
                    } catch (e: Exception) {
                        null
                    }
                },
                { refreshAppointments(vehicle.vin) },
                { refreshContracts(vehicle.vin) }
            )

            checkNotNull(batteryStatus)
            checkNotNull(kilometerReading)

            val prev = previousVehicles.firstOrNull { it.vin == vehicle.vin }
            val newVehicle = vehicle.copy(
                batteryStatus = batteryStatus!!,
                mileageKm = kilometerReading!!,
                location = location,
                lastChangeTimestamp = prev?.lastChangeTimestamp ?: System.currentTimeMillis()
            )

            // Track vehicle state on refresh also (but only if tracker is running)
            if (checkIfTrackerIsRunning(vehicle.vin).get()) {
                createVehicleChargePoint(
                    CreateChargePointParams(
                        vehicle.vin,
                        batteryStatus!!,
                        kilometerReading!!,
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
