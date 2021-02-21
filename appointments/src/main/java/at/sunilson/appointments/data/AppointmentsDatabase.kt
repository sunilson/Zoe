package at.sunilson.appointments.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.appointments.data.models.database.DatabaseAppointment
import at.sunilson.appointments.data.models.database.DatabaseService

@Database(
    entities = [DatabaseAppointment::class, DatabaseService::class],
    version = 5
)
@TypeConverters(at.sunilson.appointments.data.TypeConverters::class)
internal abstract class AppointmentsDatabase : RoomDatabase() {
    abstract fun appointmentsDao(): AppointmentsDao
    abstract fun servicesDao(): ServicesDao
}
