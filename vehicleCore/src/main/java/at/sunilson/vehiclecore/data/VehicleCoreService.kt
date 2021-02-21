package at.sunilson.vehiclecore.data

import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse
import at.sunilson.vehiclecore.data.models.cockpit.CockpitResponse
import at.sunilson.vehiclecore.data.models.location.LocationResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleCoreService {
    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/location?country=AT")
    suspend fun getVehicleLocation(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): LocationResponse

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
