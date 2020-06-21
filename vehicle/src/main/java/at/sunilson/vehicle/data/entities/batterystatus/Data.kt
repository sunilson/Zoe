package at.sunilson.vehicle.data.entities.batterystatus

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val type: String,
    val id: String,
    val attributes: Attributes
)