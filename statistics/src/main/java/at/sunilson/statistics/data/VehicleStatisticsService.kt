package at.sunilson.statistics.data

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleStatisticsService {

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/charge-history?country=AT&type=month")
    suspend fun getChargeStatistics(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): ResponseBody

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/hvac-history?country=AT&type=month")
    suspend fun getHVACStatistics(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): ResponseBody

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/charges?country=AT")
    suspend fun getChargeHistory(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): ResponseBody


    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/hvac-sessions?country=AT")
    suspend fun getHVACHistory(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): ResponseBody

}