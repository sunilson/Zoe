package at.sunilson.appointments.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class AppointmentResponse(
    val deliveryDate: String,
    val maintenance: List<NetworkAppointment>
)