package at.sunilson.scheduleCore.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.scheduleCore.domain.entities.ScheduleType

@Entity
data class DatabaseSchedule(
    @PrimaryKey
    val id: Int,
    val vin: String,
    val chargeType: ScheduleType,
    val activated: Boolean,
    val days: List<DatabaseScheduleDay>
)