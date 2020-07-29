package at.sunilson.vehiclecore.data.models.cockpit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CockpitResponse (
	val data : Data
)