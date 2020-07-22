package at.sunilson.vehiclecore.domain.entities

import java.io.Serializable

data class Location(val lat: Double, val lng: Double, val timestamp: Long) : Serializable