package at.sunilson.chargeSchedule.domain.entities

internal data class ChargeSchedule(val id: String, val activated: Boolean, val days: List<ChargeDay>)