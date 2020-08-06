package at.sunilson.chargeSchedule.domain.entities

data class ChargeSchedule(val id: String, val activated: Boolean, val days: List<ChargeDay>)