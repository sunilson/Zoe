package at.sunilson.vehiclecore.domain

import kotlinx.coroutines.flow.Flow

interface VehicleCoreRepository {
    val kamereonAccountID: String
    val selectedVehicle: Flow<String?>
    suspend fun setSelectedVehicle(vin: String)
}
