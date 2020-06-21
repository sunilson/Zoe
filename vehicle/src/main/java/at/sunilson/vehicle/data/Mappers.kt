package at.sunilson.vehicle.data

import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.data.entities.AllVehiclesResponse

fun AllVehiclesResponse.toVehicleList() = vehicleLinks.map { vehicleLink ->
    Vehicle(
        vehicleLink.vin,
        vehicleLink.vehicleDetails.model.label,
        vehicleLink.vehicleDetails.assets.first().renditions.first().url
    )
}