package at.sunilson.vehicle.domain.entities

import java.time.Duration

data class ChargeProcedure(val duration: Duration, val energyAmount: Int)