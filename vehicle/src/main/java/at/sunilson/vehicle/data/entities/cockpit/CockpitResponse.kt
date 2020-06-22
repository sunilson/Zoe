package at.sunilson.vehicle.data.entities.cockpit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CockpitResponse (
	val data : Data
)