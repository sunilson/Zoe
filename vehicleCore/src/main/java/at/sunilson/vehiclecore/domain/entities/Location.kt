package at.sunilson.vehiclecore.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.io.Serializable

@Keep
@JsonClass(generateAdapter = true)
data class Location(val lat: Double, val lng: Double, val timestamp: Long) : Serializable
