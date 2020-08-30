package at.sunilson.appointments.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class DatabaseAppointment(
    @PrimaryKey
    val id: String,
    val label: String,
    val date: String,
    val vin: String
)