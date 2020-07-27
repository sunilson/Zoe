package at.sunilson.vehiclecore.domain.entities

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Location(val lat: Double, val lng: Double, val timestamp: Long) : Serializable