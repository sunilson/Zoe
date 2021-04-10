package at.sunilson.vehicleMap.data

import at.sunilson.vehicleMap.data.models.ChargingStationsResponse
import at.sunilson.vehicleMap.domain.entities.ChargingStation
import at.sunilson.vehicleMap.domain.entities.Connection
import at.sunilson.vehicleMap.domain.entities.OpeningTime
import com.google.android.libraries.maps.model.LatLng
import java.time.DayOfWeek

internal fun ChargingStationsResponse.toEntity() = ChargingStation(
    id = iD,
    operator = operatorInfo.title,
    address = """
        ${addressInfo.title}
        ${addressInfo.addressLine1}
        ${addressInfo.postcode} ${addressInfo.town} ${addressInfo.country.iSOCode}
    """.trimIndent(),
    location = LatLng(addressInfo.latitude, addressInfo.longitude),
    connections = connections.map { Connection(it.powerKW, it.quantity, it.statusType.isOperational) }
)