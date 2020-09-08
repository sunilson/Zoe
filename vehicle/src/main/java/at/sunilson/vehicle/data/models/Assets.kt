package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Assets (
	val assetType : String,
	val renditions : List<Renditions>
)