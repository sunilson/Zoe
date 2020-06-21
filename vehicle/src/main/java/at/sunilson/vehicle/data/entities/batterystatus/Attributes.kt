package at.sunilson.vehicle.data.entities.batterystatus

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
	val chargingStatus : Int,
	val chargingRemainingTime : Int,
	val chargingInstantaneousPower : Int
)