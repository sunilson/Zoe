package at.sunilson.chargeSchedule.domain.entities

internal data class ChargeSchedule(
    val id: Int,
    val chargeType: ChargeType,
    val activated: Boolean,
    val days: List<ChargeDay>
)