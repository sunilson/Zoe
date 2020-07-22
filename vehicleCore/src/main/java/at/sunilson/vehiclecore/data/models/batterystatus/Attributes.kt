package at.sunilson.vehiclecore.data.models.batterystatus

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attributes (

	val timestamp : String,
	val batteryLevel : Int,
	val batteryTemperature : Int,
	val batteryAutonomy : Int,
	val batteryCapacity : Int,
	val batteryAvailableEnergy : Int,
	val plugStatus : Int,
	val chargingStatus : Double,
	val chargingRemainingTime : Int,
	val chargingInstantaneousPower : Float
)