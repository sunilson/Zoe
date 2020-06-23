package at.sunilson.vehicle.data.entities.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attributes (

	val gpsLatitude : Double,
	val gpsLongitude : Double,
	val lastUpdateTime : String
)