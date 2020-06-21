package at.sunilson.vehicle.data.entities.batterystatus

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BatteryStatusResponse (
	val data : Data
)