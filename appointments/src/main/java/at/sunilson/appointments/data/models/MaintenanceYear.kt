package at.sunilson.appointments.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MaintenanceYear(val year: Int, val month: Int, val maintenances: List<NetworkAppointment>)