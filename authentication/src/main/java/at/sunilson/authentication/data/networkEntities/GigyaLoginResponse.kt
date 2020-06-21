package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GigyaLoginResponse(val sessionInfo: SessionInfo)

@JsonClass(generateAdapter = true)
data class SessionInfo(val cookieName: String, val cookieValue: String)