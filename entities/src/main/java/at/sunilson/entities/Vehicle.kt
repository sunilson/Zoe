package at.sunilson.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Vehicle(
    val vin: String,
    val modelName: String,
    val imageUrl: String,
    val mileageKm: Int,
    val batteryStatus: BatteryStatus
) {
    @JsonClass(generateAdapter = true)
    data class BatteryStatus(
        val batteryLevel: Int,
        val batteryTemperature: Int,
        val pluggedIn: Boolean
    )
}