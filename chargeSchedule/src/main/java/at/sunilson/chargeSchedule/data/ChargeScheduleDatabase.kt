package at.sunilson.chargeSchedule.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.scheduleCore.data.DatabaseSchedule
import at.sunilson.scheduleCore.data.ScheduleDao

@Dao
internal interface ChargeScheduleDao : ScheduleDao

@Database(
    entities = [DatabaseSchedule::class],
    version = 5
)
@TypeConverters(at.sunilson.scheduleCore.data.TypeConverters::class)
internal abstract class ChargeScheduleDatabase : RoomDatabase() {
    abstract fun chargeScheduleDao(): ChargeScheduleDao
}
