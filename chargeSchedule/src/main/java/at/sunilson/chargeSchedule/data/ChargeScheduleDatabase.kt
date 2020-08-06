package at.sunilson.chargeSchedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeSchedule
import at.sunilson.vehiclecore.data.ZoeDatabase

@Database(
    entities = [DatabaseChargeSchedule::class],
    version = 1
)
@TypeConverters(TypeConverters::class)
internal abstract class ChargeScheduleDatabase : RoomDatabase(), ZoeDatabase {
    abstract fun chargeScheduleDao(): ChargeScheduleDao
}