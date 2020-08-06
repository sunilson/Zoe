package at.sunilson.chargeSchedule.data.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ChargeSettingsDay(val startTime: String, val duration: Int)