package at.sunilson.vehicle.data.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tcu (
	val code : String,
	val label : String,
	val group : String
)