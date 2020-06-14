package at.sunilson.vehiclecore.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vehicle(@PrimaryKey val id: String)