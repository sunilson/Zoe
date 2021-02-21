package at.sunilson.authentication.data

import at.sunilson.authentication.data.networkEntities.GigyaAccountInfo
import at.sunilson.authentication.data.networkEntities.GigyaJwtResponse
import at.sunilson.authentication.data.networkEntities.GigyaLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GigyaService {
    @FormUrlEncoded
    @POST("accounts.login")
    suspend fun gigyaLogin(
        @Field("ApiKey") apiKey: String,
        @Field("loginID") username: String,
        @Field("password") password: String
    ): GigyaLoginResponse

    @FormUrlEncoded
    @POST("accounts.getAccountInfo")
    suspend fun gigyaAccountInfo(
        @Field("ApiKey") apiKey: String,
        @Field("login_token") oauthToken: String
    ): GigyaAccountInfo

    @FormUrlEncoded
    @POST("accounts.getJWT")
    suspend fun gigyaJWT(
        @Field("ApiKey") apiKey: String,
        @Field("login_token") oauthToken: String,
        @Field("fields") fields: String
    ): GigyaJwtResponse
}
