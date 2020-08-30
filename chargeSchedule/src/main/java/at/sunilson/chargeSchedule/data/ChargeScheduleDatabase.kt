package at.sunilson.chargeSchedule.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.scheduleCore.data.ScheduleDao
import at.sunilson.scheduleCore.data.DatabaseSchedule

@Dao
internal interface ChargeScheduleDao: ScheduleDao

@Database(
    entities = [DatabaseSchedule::class],
    version = 4
)
@TypeConverters(at.sunilson.scheduleCore.data.TypeConverters::class)
internal abstract class ChargeScheduleDatabase : RoomDatabase() {
    abstract fun chargeScheduleDao(): ChargeScheduleDao
}