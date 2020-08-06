package at.sunilson.vehiclecore.data.models.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attributes (
	val gpsLatitude : Double,
	val gpsLongitude : Double,
	val lastUpdateTime : String
)