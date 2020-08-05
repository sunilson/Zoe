package at.sunilson.chargestatistics.domain.entities

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ChargingProcedure(
    val batteryLevelDifference: Int,
    val energyLevelDifference: Int,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime
)