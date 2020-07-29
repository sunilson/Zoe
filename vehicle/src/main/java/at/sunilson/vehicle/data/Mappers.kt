package at.sunilson.vehicle.data

import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehiclecore.data.models.cockpit.CockpitResponse
import at.sunilson.vehicle.data.entities.location.LocationResponse

fun AllVehiclesResponse.toVehicleList() = vehicleLinks.map { vehicleLink ->
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

fun LocationResponse.toEntity() =
    Location(
        data.attributes.gpsLatitude,
        data.attributes.gpsLongitude,
        System.currentTimeMillis()
    )