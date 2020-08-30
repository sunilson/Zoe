package at.sunilson.appointments.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.sunilson.appointments.data.models.DatabaseAppointment
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AppointmentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<DatabaseAppointment>)

    @Query("SELECT * FROM DatabaseAppointment WHERE vin = :vin")
    fun getAllAppointments(vin: String): Flow<List<DatabaseAppointment>>

}