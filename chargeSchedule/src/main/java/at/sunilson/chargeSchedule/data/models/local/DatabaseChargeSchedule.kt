package at.sunilson.chargeSchedule.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.chargeSchedule.domain.entities.ChargeType

@Entity
internal data class DatabaseChargeSchedule(
    @PrimaryKey
    val id: Int,
    val vin: String,
    val chargeType: ChargeType,
    val activated: Boolean,
    val days: List<DatabaseChargeDay>
)