package at.sunilson.vehicleDetails.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.vehicleDetails.data.models.DatabaseVehicleDetails

@Database(
    entities = [DatabaseVehicleDetails::class],
    version = 1
)
@TypeConverters(at.sunilson.vehicleDetails.data.TypeConverters::class)
internal abstract class VehicleDetailsDatabase : RoomDatabase() {
    abstract fun vehicleDetailsDao(): VehicleDetailsDao
}