package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Renditions(
    val resolutionType: String,
    val url: String
)
