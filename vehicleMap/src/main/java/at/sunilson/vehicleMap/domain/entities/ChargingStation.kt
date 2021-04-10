package at.sunilson.vehicleMap.domain.entities

import com.google.android.libraries.maps.model.LatLng
import java.time.DayOfWeek

internal data class ChargingStation(
    val id: Int,
    val operator: String,
    val address: String,
    val connections: List<Connection>,
    val location: LatLng?
)

internal data class Connection(
    val maxKW: Int,
    val quantity: Int,
    val operational: Boolean
)

internal data class OpeningTime(
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String
)
