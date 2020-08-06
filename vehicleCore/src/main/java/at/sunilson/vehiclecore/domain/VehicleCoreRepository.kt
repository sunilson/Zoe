package at.sunilson.vehiclecore.domain

import kotlinx.coroutines.flow.Flow

interface VehicleCoreRepository {
    val kamereonAccountID: String
    var selectedVehicle: String?
    val selectedVehicleFlow: Flow<String?>
}