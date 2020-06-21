package at.sunilson.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.database.databaseentities.DatabaseVehicle

@Database(
    entities = [DatabaseVehicle::class],
    version = 3
)
@TypeConverters(at.sunilson.database.TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}