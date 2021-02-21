package at.sunilson.appointments.data.models.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ServiceResponse(
    val services: List<NetworkService>
)
