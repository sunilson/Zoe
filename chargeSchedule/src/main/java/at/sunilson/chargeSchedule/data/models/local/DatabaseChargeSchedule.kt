package at.sunilson.chargeSchedule.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.chargeSchedule.domain.entities.ChargeDay

@Entity
internal data class DatabaseChargeSchedule(
    @PrimaryKey
    val id: String,
    val activated: Boolean,
    val days: List<ChargeDay>
)