package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GigyaJwtResponse(@field:Json(name = "id_token") val idToken: String)
