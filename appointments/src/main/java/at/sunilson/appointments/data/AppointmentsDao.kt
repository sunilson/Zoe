package at.sunilson.appointments.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import at.sunilson.appointments.data.models.database.DatabaseAppointment
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AppointmentsDao {

    @Transaction
    suspend fun insertAndDeleteAppointments(appointments: List<DatabaseAppointment>) {
        deleteNonExistentAppointments(appointments.map { it.id })
        insertAppointments(appointments)
    }

    @Query("DELETE FROM DatabaseAppointment WHERE id NOT IN (:ids)")
    suspend fun deleteNonExistentAppointments(ids: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<DatabaseAppointment>)

    @Query("SELECT * FROM DatabaseAppointment WHERE vin = :vin")
    fun getAllAppointments(vin: String): Flow<List<DatabaseAppointment>>
}
