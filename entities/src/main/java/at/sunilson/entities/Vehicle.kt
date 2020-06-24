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
        val pluggedIn: Boolean,
        val chargeState: ChargeState
    ) {
        enum class ChargeState(val stateCode: Double) {
            NOT_CHARGING(0.0),
            WATING_FOR_PLANNED_CHARGE(0.1),
            CHARGE_ENDED(0.2),
            WAITING_FOR_CURRENT_CHARGE(0.3),
            ENERGY_FLAP_OPENED(0.4),
            CHARGING(1.0),
            CHARGE_ERROR(-1.0)
        }
    }
}

/**
{
"batteryLevel": 30,
"batteryTemperature": 20,
"batteryAutonomy": 100,
"batteryCapacity": 0,
"batteryAvailableEnergy": 16,
"plugStatus": 1,
"chargingStatus": 1.0,
"chargingRemainingTime": 310,
"chargingInstantaneousPower": 72.3
}
**/