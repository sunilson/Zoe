package at.sunilson.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.database.databaseentities.DatabaseVehicle
import timber.log.Timber

@Database(
    entities = [DatabaseVehicle::class],
    version = 3
)
@TypeConverters(at.sunilson.database.TypeConverters::class)
abstract class Database : RoomDatabase(), ZoeDatabase {
    abstract fun vehicleDao(): VehicleDao

    override fun clearEverything() {
        try {
            clearAllTables()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }
}

interface ZoeDatabase {
    fun clearEverything()
}