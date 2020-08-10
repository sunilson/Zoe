package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsResponse
import at.sunilson.networkingcore.KamereonPostBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

internal interface ChargeScheduleService {
    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/charging-settings?country=AT")
    suspend fun getChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ChargeSettingsResponse

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v2/cars/{vin}/actions/charge-schedule?country=AT")
    suspend fun setChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/actions/charge-mode?country=AT")
    suspend fun setChargeMode(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody
}