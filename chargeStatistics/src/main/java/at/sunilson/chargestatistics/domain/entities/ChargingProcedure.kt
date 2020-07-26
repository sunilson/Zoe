package at.sunilson.chargestatistics.domain.entities

import java.time.LocalDateTime

data class ChargingProcedure(
    val batteryLevelDifference: Int,
    val energyLevelDifference: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)