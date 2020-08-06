package at.sunilson.chargeSchedule.data.models.remote

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsDay
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ChargeSettingsSchedule(
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