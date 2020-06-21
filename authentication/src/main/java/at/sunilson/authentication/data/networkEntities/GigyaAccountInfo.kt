package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GigyaAccountInfo(val data: Data)

@JsonClass(generateAdapter = true)
data class Data(val personId: String, val gigyaDataCenter: String)