package at.sunilson.vehicleMap.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
internal data class ReachableAreaResponse(
    val boundingBox: NetworkBoundingBox,
    val encodedGeometry: String
)

@JsonClass(generateAdapter = true)
@Keep
data class NetworkBoundingBox(
    val minLon: Double,
    val minLat: Double,
    val maxLon: Double,
    val maxLat: Double
)
