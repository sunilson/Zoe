package at.sunilson.vehiclecore.data

import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse
import at.sunilson.vehiclecore.data.models.cockpit.CockpitResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleCoreService {

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

}