package at.sunilson.authentication.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

internal interface KamereonService {
    @GET("persons/{person_id}?country=AT")
    suspend fun kamereonAccount(
        @Path("person_id") personId: String,
        @Header("x-gigya-id_token") gigyaIdToken: String,
        @Header("apikey") apiKey: String
    )

    @GET("accounts/{account_id}/kamereon/token?country=AT")
    suspend fun kamereonTokens(
        @Path("account_id") accountId: String,
        @Header("x-gigya-id_token") gigyaIdToken: String,
        @Header("apikey") apiKey: String
    )
}