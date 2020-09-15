package at.sunilson.chargestatistics.domain.entities

import androidx.annotation.IntRange
import java.time.ZonedDateTime
import kotlin.math.max

data class ChargingProcedure(
    val startBatteryLevel: Int,
    val endBatteryLevel: Int,
    val energyLevelDifference: Int,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime
) {
    @get:IntRange(from = 0L)
    val batteryLevelDifference: Int
        get() = max(0, endBatteryLevel - startBatteryLevel)
}