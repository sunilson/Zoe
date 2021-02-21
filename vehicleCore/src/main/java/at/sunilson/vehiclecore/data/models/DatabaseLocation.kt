package at.sunilson.vehiclecore.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseLocation(
    @PrimaryKey val vin: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
