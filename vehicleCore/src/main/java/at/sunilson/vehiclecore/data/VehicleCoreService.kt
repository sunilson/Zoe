package at.sunilson.vehiclecore.data

import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse
import at.sunilson.vehiclecore.data.models.cockpit.CockpitResponse
import at.sunilson.vehiclecore.data.models.location.LocationResponse
import retrofit2.http.GET
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

}