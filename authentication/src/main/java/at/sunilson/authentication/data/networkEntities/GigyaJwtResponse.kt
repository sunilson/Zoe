package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.Json

data class GigyaJwtResponse(@field:Json(name = "id_token") val idToken: String)