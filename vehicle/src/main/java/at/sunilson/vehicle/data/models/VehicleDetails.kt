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
    val vcd: String,
    val assets: List<Assets>?,
    val yearsOfMaintenance: Int,
    val connectivityTechnology: String,
    val easyConnectStore: Boolean,
    val electrical: Boolean,
    val rlinkStore: Boolean,
    val deliveryDate: String,
    val retrievedFromDhs: Boolean,
    val engineEnergyType: String
)