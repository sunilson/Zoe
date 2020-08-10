package at.sunilson.chargeSchedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeSchedule

@Database(
    entities = [DatabaseChargeSchedule::class],
    version = 3
)
@TypeConverters(at.sunilson.chargeSchedule.data.TypeConverters::class)
internal abstract class ChargeScheduleDatabase : RoomDatabase() {
    abstract fun chargeScheduleDao(): ChargeScheduleDao
}