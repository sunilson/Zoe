package at.sunilson.authentication.domain.entities

data class LoginParams(val username: String, val password: String, val stayLoggedIn: Boolean)
