package at.sunilson.vehicleMap.domain.entities

import com.google.android.libraries.maps.model.LatLng
import java.time.DayOfWeek
import java.time.LocalTime

internal data class ChargingStation(
    val id: String,
    val name: String,
    val type: String,
    val address: String,
    val powerLevels: List<Float>,
    val plugs: List<String>,
    val paymentInfo: String,
    val paymentModes: List<String>,
    val availableSpots: Int,
    val usabilityStatus: String,
    val openingTimes: List<OpeningTime>,
    val location: LatLng?
)

internal data class OpeningTime(
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String
)