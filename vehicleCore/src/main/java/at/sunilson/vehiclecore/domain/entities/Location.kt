package at.sunilson.vehiclecore.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Keep
@JsonClass(generateAdapter = true)
data class Location(val lat: Double, val lng: Double, val timestamp: Long) : Serializable