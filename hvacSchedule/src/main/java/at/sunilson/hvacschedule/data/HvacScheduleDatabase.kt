package at.sunilson.hvacschedule.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.scheduleCore.data.DatabaseSchedule
import at.sunilson.scheduleCore.data.ScheduleDao

@Dao
internal interface HvacScheduleDao: ScheduleDao

@Database(
    entities = [DatabaseSchedule::class],
    version = 2
)
@TypeConverters(at.sunilson.scheduleCore.data.TypeConverters::class)
internal abstract class HvacScheduleDatabase : RoomDatabase() {
    abstract fun hvacScheduleDao(): HvacScheduleDao
}