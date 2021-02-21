package at.sunilson.vehicle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleLinks(
    val brand: String,
    val vin: String,
    val status: String,
    val garageBrand: String,
    val annualMileage: Int,
    val startDate: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val vehicleDetails: VehicleDetails
)
