package at.sunilson.vehicleMap.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class NetworkCharingStation(
    val type: String,
    val id: String,
    val name: String,
    val address: Address,
    val latitude: Double?,
    val longitude: Double?,
    val accessibilityType: String,
    val openingTime: List<OpeningTime>,
    val powerLevels: List<Float>,
    val plugs: List<String>,
    val availabilityStatus: AvailabilityStatus,
    val payment: Payment
) {

    @JsonClass(generateAdapter = true)
    @Keep
    data class OpeningTime(
        val dayOfWeek: Int,
        val startTime: String,
        val endTime: String
    )


    @JsonClass(generateAdapter = true)
    @Keep
    data class Payment(
        val info: String,
        val paymentModes: List<String>
    )

    @JsonClass(generateAdapter = true)
    @Keep
    data class AvailabilityStatus(
        val usabilityStatus: String,
        val availableSpotsNumber: Int
    )

    @JsonClass(generateAdapter = true)
    @Keep
    data class Address(
        val streetNumber: String,
        val streetName: String,
        val postCode: String,
        val city: String,
        val country: String
    )
}