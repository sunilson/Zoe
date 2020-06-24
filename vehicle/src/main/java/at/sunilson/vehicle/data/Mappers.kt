package at.sunilson.vehicle.data

import at.sunilson.entities.Location
import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehicle.data.entities.batterystatus.BatteryStatusResponse
import at.sunilson.vehicle.data.entities.cockpit.CockpitResponse
import at.sunilson.vehicle.data.entities.location.LocationResponse

fun AllVehiclesResponse.toVehicleList() = vehicleLinks.map { vehicleLink ->
    Vehicle(
        vehicleLink.vin,
        vehicleLink.vehicleDetails.model.label,
        vehicleLink.vehicleDetails.assets.first().renditions.first().url,
        0,
        Vehicle.BatteryStatus(0, 0, false, Vehicle.BatteryStatus.ChargeState.NOT_CHARGING)
    )
}

fun BatteryStatusResponse.toEntity() =
    Vehicle.BatteryStatus(
        data.attributes.batteryLevel,
        data.attributes.batteryTemperature,
        data.attributes.plugStatus == 1,
        Vehicle.BatteryStatus.ChargeState.values()
            .firstOrNull { it.stateCode == data.attributes.chargingStatus }
            ?: Vehicle.BatteryStatus.ChargeState.NOT_CHARGING
    )

fun CockpitResponse.toEntity() = data.attributes.totalMileage

fun LocationResponse.toEntity() = Location(
    data.attributes.gpsLatitude,
    data.attributes.gpsLongitude,
    System.currentTimeMillis()
)