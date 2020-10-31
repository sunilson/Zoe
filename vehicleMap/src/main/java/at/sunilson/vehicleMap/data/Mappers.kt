package at.sunilson.vehicleMap.data

import at.sunilson.vehicleMap.data.models.NetworkCharingStation
import at.sunilson.vehicleMap.domain.entities.ChargingStation
import at.sunilson.vehicleMap.domain.entities.OpeningTime
import com.google.android.libraries.maps.model.LatLng
import java.time.DayOfWeek
import java.time.LocalTime

internal fun NetworkCharingStation.toEntity() = ChargingStation(
    id,
    name,
    type,
    address.run {
        """
          $streetName $streetNumber
          $city $postCode
          $country
      """.trimIndent()
    },
    powerLevels,
    plugs,
    payment.info,
    payment.paymentModes,
    availabilityStatus.availableSpotsNumber,
    availabilityStatus.usabilityStatus,
    openingTime.map {
        OpeningTime(
            DayOfWeek.of(it.dayOfWeek),
            it.startTime,
            it.endTime
        )
    },
    if (latitude != null && longitude != null) LatLng(latitude, longitude) else null
)