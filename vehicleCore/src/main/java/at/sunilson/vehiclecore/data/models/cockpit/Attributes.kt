package at.sunilson.vehiclecore.data.models.cockpit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attributes (

	val fuelQuantity : Int,
	val fuelAutonomy : Int,
	val totalMileage : Int
)