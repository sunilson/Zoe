package at.sunilson.vehicleMap.domain.entities

import at.sunilson.vehiclecore.domain.entities.Location

data class GetReachableAreaParams(val vin: String, val location: Location)
