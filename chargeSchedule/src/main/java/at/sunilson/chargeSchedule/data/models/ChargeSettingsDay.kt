package at.sunilson.chargeSchedule.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChargeSettingsDay(val startTime: String, val duration: Int)