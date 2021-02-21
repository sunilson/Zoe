package at.sunilson.vehiclecore.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
@Keep
data class Vehicle(
    val vin: String,
    val modelName: String,
    val modelVersion: String,
    val imageUrl: String,
    val mileageKm: Int,
    val batteryStatus: BatteryStatus,
    val lastChangeTimestamp: Long,
    val brand: String,
    val tcu: String,
    val batteryLabel: String,
    val radioType: String,
    val gearBox: String,
    val yearsOfMaintainance: Int,
    val connectivityTechnology: String,
    val annualMileage: Int,
    val location: Location?
) : Serializable {
    @JsonClass(generateAdapter = true)
    @Keep
    data class BatteryStatus(
        val batteryLevel: Int,
        val batteryTemperature: Int,
        val remainingRange: Int,
        val availableEnery: Int,
        val pluggedIn: Boolean,
        val chargeState: ChargeState,
        val chargeSpeed: Float,
        val remainingChargeTime: Int
    ) : Serializable {

        val isCharging: Boolean
            get() = chargeState == ChargeState.CHARGING

        val batteryCapacity: Int
            get() = ((availableEnery.toFloat() / batteryLevel) * 100).toInt()

        @Keep
        enum class ChargeState(val stateCode: Double) : Serializable {
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
