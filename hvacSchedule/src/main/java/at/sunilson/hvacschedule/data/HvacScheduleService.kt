package at.sunilson.hvacschedule.data

import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.scheduleCore.data.remote.ScheduleResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface HvacScheduleService {
    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/hvac-settings?country=AT")
    suspend fun getChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ScheduleResponse

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v2/cars/{vin}/actions/hvac-schedule?country=AT")
    suspend fun setChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody


    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/actions/hvac-mode?country=AT")
    suspend fun setChargeMode(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody

}