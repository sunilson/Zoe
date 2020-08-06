package at.sunilson.chargeSchedule.data.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ChargeSettingsAttributes(val mode: String, val schedules: List<ChargeSettingsSchedule>)