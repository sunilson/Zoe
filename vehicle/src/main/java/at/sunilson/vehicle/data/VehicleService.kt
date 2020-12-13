package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.models.AllVehiclesResponse
import at.sunilson.networkingcore.KamereonPostBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleService {
    @GET("accounts/{accountId}/vehicles?country=AT")
    suspend fun getAllVehicles(@Path("accountId") accountId: String): AllVehiclesResponse

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/actions/charging-start?country=AT")
    suspend fun startCharging(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody
}