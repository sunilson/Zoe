package at.sunilson.vehiclecore.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.vehiclecore.data.models.DatabaseLocation
import at.sunilson.vehiclecore.data.models.DatabaseVehicle
import timber.log.Timber

@Database(
    entities = [DatabaseVehicle::class, DatabaseLocation::class],
    version = 8
)
@TypeConverters(at.sunilson.vehiclecore.data.TypeConverters::class)
abstract class VehicleDatabase : RoomDatabase(), ZoeDatabase {
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
