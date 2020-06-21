package at.sunilson.vehicle.data

import at.sunilson.vehicle.data.entities.AllVehiclesResponse
import at.sunilson.vehicle.data.entities.batterystatus.BatteryStatusResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleService {

    @GET("accounts/{accountId}/vehicles?country=AT")
    suspend fun getAllVehicles(@Path("accountId") accountId: String): AllVehiclesResponse

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v2/cars/{vin}/battery-status?country=AT")
    suspend fun getBatteryStatus(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): BatteryStatusResponse
}