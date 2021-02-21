package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectedDriver(
    val role: String,
    val createdDate: String,
    val lastModifiedDate: String
)
