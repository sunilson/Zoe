package at.sunilson.vehiclecore.domain.entities

import androidx.annotation.Keep
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Keep
data class Location(val lat: Double, val lng: Double, val timestamp: ZonedDateTime) : Serializable