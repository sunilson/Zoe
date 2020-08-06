package at.sunilson.vehiclecore.data.models.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse (
	val data : Data
)