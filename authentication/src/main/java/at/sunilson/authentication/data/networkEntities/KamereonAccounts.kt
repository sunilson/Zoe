package at.sunilson.authentication.data.networkEntities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KamereonAccounts(val accounts: List<KamereonAccount>)

@JsonClass(generateAdapter = true)
data class KamereonAccount(val accountId: String)
