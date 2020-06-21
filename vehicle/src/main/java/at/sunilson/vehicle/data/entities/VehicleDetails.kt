package at.sunilson.vehicle.data.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDetails (
	val vin : String,
	val engineType : String,
	val engineRatio : Int,
	val deliveryCountry : DeliveryCountry,
	val family : Family,
	val tcu : Tcu,
	val navigationAssistanceLevel : NavigationAssistanceLevel,
	val battery : Battery,
	val radioType : RadioType,
	val registrationCountry : RegistrationCountry,
	val brand : Brand,
	val model : Model,
	val gearbox : Gearbox,
	val version : Version,
	val energy : Energy,
	val registrationNumber : String,
	val vcd : String,
	val assets : List<Assets>,
	val yearsOfMaintenance : Int,
	val connectivityTechnology : String,
	val easyConnectStore : Boolean,
	val electrical : Boolean,
	val rlinkStore : Boolean,
	val deliveryDate : String,
	val retrievedFromDhs : Boolean,
	val engineEnergyType : String
)