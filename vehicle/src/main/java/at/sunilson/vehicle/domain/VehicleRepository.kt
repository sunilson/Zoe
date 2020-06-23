package at.sunilson.vehicle.domain

import at.sunilson.entities.Location
import at.sunilson.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.Flow

internal interface VehicleRepository {
    var selectedVehicle: String?
    suspend fun getRefreshedVehicles(): SuspendableResult<List<Vehicle>, Exception>
    suspend fun getBatteryStatus(vehicleVin: String): SuspendableResult<Vehicle.BatteryStatus, Exception>
    suspend fun getKilometerReading(vehicleVin: String): SuspendableResult<Int, Exception>
    suspend fun saveVehiclesToLocalStorage(vehicles: List<Vehicle>): SuspendableResult<Unit, Exception>
    suspend fun startClimeateControl(vehicleVin: String): SuspendableResult<Unit, Exception>
    suspend fun locateVehicle(vehicleVin: String): SuspendableResult<Location, Exception>
    fun getVehicle(id: String): Flow<Vehicle?>
    fun getAllVehicles(): Flow<List<Vehicle>>
}