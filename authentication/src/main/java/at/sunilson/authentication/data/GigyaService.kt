package at.sunilson.authentication.data

import at.sunilson.authentication.data.networkEntities.GigyaAccountInfo
import at.sunilson.authentication.data.networkEntities.GigyaLoginResponse
import retrofit2.http.Field
import retrofit2.http.POST

interface GigyaService {
    @POST("accounts.login")
    suspend fun gigyaLogin(
        @Field("ApiKey") apiKey: String,
        @Field("loginID") username: String,
        @Field("password") password: String
    ): GigyaLoginResponse

    @POST("accounts.getAccountInfo")
    suspend fun gigyaAccountInfo(@Field("oauth_token") oauthToken: String): GigyaAccountInfo

    @POST("accounts.getJWT")
    suspend fun gigyaJWT(
        @Field("oauth_token") oauthToken: String,
        @Field("fields") fields: String
    ): String
}