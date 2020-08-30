package at.sunilson.appointments.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class AppointmentResponse(
    val previousMaintenances: List<MaintenanceYear>,
    val upcomingMaintenances: List<MaintenanceYear>
)