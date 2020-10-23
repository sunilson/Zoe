package at.sunilson.appointments.data.models.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkService(
    val date: String,
    val origin: String,
    val mileage: Int,
    val dealerId: String,
    val title: ServiceCode,
    val subTitle: ServiceCode
)

@JsonClass(generateAdapter = true)
internal data class ServiceCode(
    val code: String
)