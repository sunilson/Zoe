package at.sunilson.vehicleDetails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseVehicleDetails(@PrimaryKey val vin: String)