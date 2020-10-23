package at.sunilson.appointments.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseService(@PrimaryKey val id: String, val date: String, val vin: String)