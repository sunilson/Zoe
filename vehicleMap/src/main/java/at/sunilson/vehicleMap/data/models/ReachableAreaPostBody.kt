package at.sunilson.vehicleMap.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class ReachableAreaPostBody(
    val vin: String,
    val initBatLvl: Int,
    val temperature: Int,
    val longitude: Double,
    val latitude: Double,
    val geo: Boolean = false,
    val egeo: Boolean = true,
    val extraPayload: Int = 0
)
