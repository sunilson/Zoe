package at.sunilson.vehicle.domain

import at.sunilson.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    var selectedVehicle: String?
    suspend fun getRefreshedVehicles(): SuspendableResult<List<Vehicle>, Exception>
    suspend fun getBatteryStatus(vehicleVin: String): SuspendableResult<Vehicle.BatteryStatus, Exception>
    suspend fun saveVehiclesToLocalStorage(vehicles: List<Vehicle>): SuspendableResult<Unit, Exception>
    fun getVehicle(id: String): Flow<Vehicle?>
    fun getAllVehicles(): Flow<List<Vehicle>>
}