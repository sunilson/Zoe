package at.sunilson.chargetracking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.chargetracking.data.models.ChargeTrackingPoint

@Database(
    entities = [ChargeTrackingPoint::class],
    version = 1
)
@TypeConverters(at.sunilson.chargetracking.data.TypeConverters::class)
abstract class Database : RoomDatabase() {
    internal abstract fun chargeTrackingDao(): ChargeTrackingDao
}