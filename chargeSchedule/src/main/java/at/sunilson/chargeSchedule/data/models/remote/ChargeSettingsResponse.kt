package at.sunilson.chargeSchedule.data.models.remote

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ChargeSettingsResponse(val data: ChargeSettingsData)