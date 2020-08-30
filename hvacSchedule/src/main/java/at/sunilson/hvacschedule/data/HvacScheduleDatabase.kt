package at.sunilson.hvacschedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.scheduleCore.data.DatabaseSchedule
import at.sunilson.scheduleCore.data.ScheduleDao

@Database(
    entities = [DatabaseSchedule::class],
    version = 1
)
@TypeConverters(at.sunilson.scheduleCore.data.TypeConverters::class)
internal abstract class HvacScheduleDatabase : RoomDatabase() {
    abstract fun hvacScheduleDao(): ScheduleDao
}