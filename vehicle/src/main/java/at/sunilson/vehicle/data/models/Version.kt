package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Version (
	val code : String
)