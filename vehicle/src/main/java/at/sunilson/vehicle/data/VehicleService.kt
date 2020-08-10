package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.networkingcore.KamereonPostBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleService {

    @GET("accounts/{accountId}/vehicles?country=AT")
    suspend fun getAllVehicles(@Path("accountId") accountId: String): AllVehiclesResponse

    @Headers("Content-Type: application/vnd.api+json")
    @POST("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/actions/hvac-start?country=AT")
    suspend fun startHVAC(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Body kamereonPostBody: KamereonPostBody
    ): ResponseBody

}