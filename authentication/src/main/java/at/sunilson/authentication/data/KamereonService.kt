package at.sunilson.authentication.data

import at.sunilson.authentication.data.networkEntities.KamereonAccounts
import at.sunilson.authentication.data.networkEntities.KamereonHeader
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface KamereonService {
    @GET("persons/{person_id}?country=AT")
    suspend fun kamereonAccount(
        @Path("person_id") personId: String,
        @HeaderMap headers: KamereonHeader
    ): KamereonAccounts

    @GET("accounts/{account_id}/kamereon/token?country=AT")
    suspend fun kamereonTokens(
        @Path("account_id") accountId: String,
        @HeaderMap headers: KamereonHeader
    )
}