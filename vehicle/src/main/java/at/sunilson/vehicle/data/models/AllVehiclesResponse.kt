package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllVehiclesResponse(
    val accountId: String,
    val country: String,
    val vehicleLinks: List<VehicleLinks>
)
