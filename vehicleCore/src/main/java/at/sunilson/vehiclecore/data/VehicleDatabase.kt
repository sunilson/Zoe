package at.sunilson.vehiclecore.data

import androidx.room.Database
import androidx.room.RoomDatabase
import at.sunilson.vehiclecore.data.entities.Vehicle

@Database(
    entities = [Vehicle::class],
    version = 1
)
internal abstract class VehicleDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}