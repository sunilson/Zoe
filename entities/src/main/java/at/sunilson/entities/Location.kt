package at.sunilson.entities

import java.io.Serializable

data class Location(val lat: Double, val lng: Double, val timestamp: Long) : Serializable