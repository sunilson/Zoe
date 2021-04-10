package at.sunilson.vehicleDetails.data

import at.sunilson.vehicleDetails.data.models.VehicleDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface VehicleDetailsService {
    @GET("accounts/{accountId}/vehicles/{vin}/details?country=DE&lang=de&specifications=true")
    suspend fun getVehicleDetails(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): VehicleDetailsResponse
}
