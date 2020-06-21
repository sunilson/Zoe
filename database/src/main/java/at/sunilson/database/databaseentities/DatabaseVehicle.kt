package at.sunilson.database.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.entities.Vehicle

@Entity
data class DatabaseVehicle(@PrimaryKey val vin: String, val vehicle: Vehicle)