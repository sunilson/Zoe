package at.sunilson.vehiclecore.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.vehiclecore.domain.entities.Vehicle

@Entity
data class DatabaseVehicle(@PrimaryKey val vin: String, val vehicle: at.sunilson.vehiclecore.domain.entities.Vehicle)