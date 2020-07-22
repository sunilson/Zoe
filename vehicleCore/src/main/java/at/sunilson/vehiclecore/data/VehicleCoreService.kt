package at.sunilson.vehiclecore.data

import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleCoreService {

    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v2/cars/{vin}/battery-status?country=AT")
    suspend fun getBatteryStatus(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): BatteryStatusResponse

}