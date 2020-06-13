package at.sunilson.authentication.data.networkEntities

data class GigyaLoginResponse(val sessionInfo: SessionInfo)
data class SessionInfo(val cookieName: String, val cookieValue: String)