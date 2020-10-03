package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDetails(
    val vin: String,
    val engineType: String,
    val engineRatio: Int,
    val battery: Battery,
    val brand: Brand,
    val model: Model,
    val version: Version,
    val registrationNumber: String,
    val assets: List<Assets>?,
    val yearsOfMaintenance: Int,
    val connectivityTechnology: String,
    val easyConnectStore: Boolean,
    val electrical: Boolean,
    val deliveryDate: String,
    val engineEnergyType: String
)