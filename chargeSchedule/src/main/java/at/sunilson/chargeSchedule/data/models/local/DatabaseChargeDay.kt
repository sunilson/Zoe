package at.sunilson.chargeSchedule.data.models.local

import at.sunilson.chargeSchedule.domain.entities.ChargeDay
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class DatabaseChargeDay(val dayOfWeek: ChargeDay.WeekDay, val startTime: Int, val duration: Int)