package at.sunilson.chargetracking.domain.entities

import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle

data class ChargeTrackingPoint(
    val vehicleId: String,
    val timestamp: Long,
    val batteryStatus: Vehicle.BatteryStatus,
    val mileageKm: Int,
    val location: Location? = null
)