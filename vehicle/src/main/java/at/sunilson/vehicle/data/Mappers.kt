package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehiclecore.data.models.location.LocationResponse
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import java.time.ZoneId
import java.time.ZonedDateTime

fun AllVehiclesResponse.toVehicleList() = vehicleLinks
    .filter { it.vehicleDetails.electrical }
    .map { vehicleLink ->
        Vehicle(
            vehicleLink.vin,
            vehicleLink.vehicleDetails.model.label,
            vehicleLink.vehicleDetails.assets.first().renditions.first().url,
            0,
            Vehicle.BatteryStatus(
                0,
                0,
                0,
                0,
                false,
                Vehicle.BatteryStatus.ChargeState.NOT_CHARGING,
                0f,
                0
            )
        )
    }