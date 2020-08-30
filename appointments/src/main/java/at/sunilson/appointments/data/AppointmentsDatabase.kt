package at.sunilson.appointments.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.appointments.data.models.DatabaseAppointment

@Database(
    entities = [DatabaseAppointment::class],
    version = 4
)
@TypeConverters(at.sunilson.appointments.data.TypeConverters::class)
internal abstract class AppointmentsDatabase : RoomDatabase() {
    abstract fun appointmentsDao(): AppointmentsDao
}