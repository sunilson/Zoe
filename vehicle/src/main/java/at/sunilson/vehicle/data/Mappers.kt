package at.sunilson.vehicle.data

import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehicle.data.entities.batterystatus.BatteryStatusResponse
import at.sunilson.vehicle.data.entities.cockpit.CockpitResponse

fun AllVehiclesResponse.toVehicleList() = vehicleLinks.map { vehicleLink ->
    Vehicle(
        vehicleLink.vin,
        vehicleLink.vehicleDetails.model.label,
        vehicleLink.vehicleDetails.assets.first().renditions.first().url,
        0,
        Vehicle.BatteryStatus(0, 0)
    )
}

fun BatteryStatusResponse.toEntity() =
    Vehicle.BatteryStatus(data.attributes.batteryLevel, data.attributes.batteryTemperature)

fun CockpitResponse.toEntity() = data.attributes.totalMileage