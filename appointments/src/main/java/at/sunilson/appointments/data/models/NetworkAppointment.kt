package at.sunilson.appointments.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkAppointment(val label: String, val mileage: String, val years: IntArray)