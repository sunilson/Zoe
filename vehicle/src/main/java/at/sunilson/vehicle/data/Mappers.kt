package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehiclecore.domain.entities.Vehicle

fun AllVehiclesResponse.toVehicleList() = vehicleLinks
    .filter { it.vehicleDetails.electrical }
    .map { vehicleLink ->
        Vehicle(
            vehicleLink.vin,
            vehicleLink.vehicleDetails.model.label,
            vehicleLink.vehicleDetails.assets?.firstOrNull()?.renditions?.firstOrNull()?.url
                ?: "https://i.imgur.com/ajoEQXB.png",
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