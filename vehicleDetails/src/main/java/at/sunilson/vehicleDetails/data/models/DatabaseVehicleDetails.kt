package at.sunilson.vehicleDetails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry

@Entity
data class DatabaseVehicleDetails(
    @PrimaryKey val vin: String,
    val vehicleDetails: List<VehicleDetailsEntry>
)
