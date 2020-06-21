package at.sunilson.authentication.data.networkEntities

//TODO Move this to networkingCore or something of that kind
class KamereonHeader(gigyaJwt: String, apiKey: String, accessToken: String? = null) :
    MutableMap<String, String> by mutableMapOf() {
    init {
        put("x-gigya-id_token", gigyaJwt)
        put("apikey", apiKey)
        if (accessToken != null) put("x-kamereon-authorization", accessToken)
    }
}