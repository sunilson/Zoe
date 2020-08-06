package at.sunilson.chargeSchedule.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChargeSettingsSchedule(
    val id: String,
    val activated: Boolean,
    val monday: ChargeSettingsDay?,
    val tuesday: ChargeSettingsDay?,
    val wednesday: ChargeSettingsDay?,
    val thursday: ChargeSettingsDay?,
    val friday: ChargeSettingsDay?,
    val saturday: ChargeSettingsDay?,
    val sunday: ChargeSettingsDay?
)