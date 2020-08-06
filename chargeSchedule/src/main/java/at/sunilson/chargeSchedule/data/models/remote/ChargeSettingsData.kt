package at.sunilson.chargeSchedule.data.models.remote

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsAttributes
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ChargeSettingsData(
    val id: String,
    val type: String,
    val attributes: ChargeSettingsAttributes
)