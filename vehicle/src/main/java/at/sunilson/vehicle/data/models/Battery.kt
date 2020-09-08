package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Battery (
	val code : String,
	val label : String,
	val group : Int
)