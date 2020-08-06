package at.sunilson.chargeSchedule.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChargeSettingsResponse(val id: String, val type: String, val attributes: Attributes) {
    @JsonClass(generateAdapter = true)
    data class Attributes(val mode: String, val schedules: List<ChargeSettingsSchedule>)
}