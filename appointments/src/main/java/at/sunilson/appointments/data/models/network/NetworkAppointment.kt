package at.sunilson.appointments.data.models.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkAppointment(
    val label: String,
    val code: Int,
    val group: String,
    val date: String
)