package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeliveryCountry(
    val code: String,
    val label: String
)
