package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KamereonTokensResponse(val accessToken: String, val refreshToken: String)
