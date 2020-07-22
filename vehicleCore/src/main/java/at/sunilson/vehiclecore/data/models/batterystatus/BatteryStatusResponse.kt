package at.sunilson.vehiclecore.data.models.batterystatus

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BatteryStatusResponse (
	val data : Data
)