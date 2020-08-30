package at.sunilson.appointments.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class DatabaseAppointment(
    val startDate: String,
    @PrimaryKey
    val label: String,
    val mileage: String,
    val years: List<Int>,
    val vin: String
)