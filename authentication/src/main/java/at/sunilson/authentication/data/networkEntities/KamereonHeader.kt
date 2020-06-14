package at.sunilson.authentication.data.networkEntities

class KamereonHeader(gigyaJwt: String, apiKey: String) : MutableMap<String, String> by mutableMapOf() {
    init {
        put("x-gigya-id_token", gigyaJwt)
        put("apikey", apiKey)
    }
}