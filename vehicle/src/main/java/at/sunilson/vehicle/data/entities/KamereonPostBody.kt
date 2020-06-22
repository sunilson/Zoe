package at.sunilson.vehicle.data.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KamereonPostBody(val data: Data) {
    @JsonClass(generateAdapter = true)
    data class Data(val type: String, val attributes: Map<String, Any?>)
}
