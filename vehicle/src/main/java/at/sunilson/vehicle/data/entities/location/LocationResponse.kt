package at.sunilson.vehicle.data.entities.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse (
	val data : Data
)