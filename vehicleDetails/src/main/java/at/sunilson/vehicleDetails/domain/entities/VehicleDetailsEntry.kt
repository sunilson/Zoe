package at.sunilson.vehicleDetails.domain.entities

import com.squareup.moshi.JsonClass
import dev.zacsweers.moshisealed.annotations.TypeLabel

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class VehicleDetailsEntry {
    @TypeLabel("image")
    @JsonClass(generateAdapter = true)
    data class Image(val url: String) : VehicleDetailsEntry()

    @TypeLabel("equipment")
    @JsonClass(generateAdapter = true)
    data class Equipment(val code: String, val group: String, val label: String) :
        VehicleDetailsEntry()

    @TypeLabel("information")
    @JsonClass(generateAdapter = true)
    data class Information(val code: String, val title: String, val description: String) :
        VehicleDetailsEntry()
}
