package at.sunilson.contracts.data

import at.sunilson.contracts.data.models.NetworkContract
import retrofit2.http.GET
import retrofit2.http.Path

@Suppress("MaxLineLength", "MaximumLineLength")
internal interface ContractsService {
    @GET("accounts/{accountId}/vehicles/{vin}/contracts?connectedServicesContracts=true&warranty=true&warrantyMaintenanceContracts=true&country=AT&lang=de&brand=RENAULT")
    suspend fun getContracts(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): List<NetworkContract>
}
