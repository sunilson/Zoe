package at.sunilson.chargetracking.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.vehiclecore.domain.entities.Vehicle

@Entity
data class ChargeTrackingPoint(
    val vehicleId: String,
    val timestamp: Long,
    val batteryStatus: Vehicle.BatteryStatus,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)