package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehicle.data.entities.KamereonPostBody
import at.sunilson.vehicle.data.entities.batterystatus.BatteryStatusResponse
import at.sunilson.vehicle.data.entities.cockpit.CockpitResponse
import at.sunilson.vehicle.data.entities.location.LocationResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleService {

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/location?country=AT")
    suspend fun getVehicleLocation(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): LocationResponse

    @GET("accounts/{accountId}/vehicles?country=AT")
    suspend fun getAllVehicles(@Path("accountId") accountId: String): AllVehiclesResponse

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v2/cars/{vin}/battery-status?country=AT")
    suspend fun getBatteryStatus(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): BatteryStatusResponse

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/cockpit?country=AT")
    suspend fun getKilometerReading(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): CockpitResponse

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/hvac-status?country=AT")
    suspend fun getHVACStatus(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ResponseBody

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/actions/hvac-start?country=AT")
    suspend fun startHVAC(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody
}