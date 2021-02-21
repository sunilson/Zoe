package at.sunilson.vehicleDetails.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class VehicleDetailsResponse(
    val vin: String,
    val engineType: String,
    val engineRatio: String,
    val deliveryCountry: Map<String, String>,
    val family: Map<String, String>,
    val brand: Map<String, String>,
    val commercialModel: Map<String, String>,
    val harmony: Map<String, String>,
    val trim: Map<String, String>,
    val colorMarketing: Map<String, String>,
    val energy: Map<String, String>,
    val equipments: List<VehicleEquipment>
)

@JsonClass(generateAdapter = true)
internal data class VehicleEquipment(val code: String, val label: String, val group: String)
