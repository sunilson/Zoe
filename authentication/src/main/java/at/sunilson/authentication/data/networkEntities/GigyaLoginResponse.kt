package at.sunilson.authentication.data.networkEntities

internal data class GigyaLoginResponse(val sessionInfo: SessionInfo)
internal data class SessionInfo(val cookieName: String, val cookieValue: String)