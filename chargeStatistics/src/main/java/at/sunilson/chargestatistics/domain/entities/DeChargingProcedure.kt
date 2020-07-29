package at.sunilson.chargestatistics.domain.entities

import java.time.LocalDateTime

data class DeChargingProcedure(
    val batteryLevelDifference: Int,
    val energyLevelDifference: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val kmDifference: Int
)