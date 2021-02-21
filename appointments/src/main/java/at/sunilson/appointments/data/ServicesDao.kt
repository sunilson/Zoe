package at.sunilson.appointments.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import at.sunilson.appointments.data.models.database.DatabaseService
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicesDao {

    @Transaction
    suspend fun insertAndDeleteServices(services: List<DatabaseService>) {
        deleteNonExistentServices(services.map { it.id })
        insertServices(services)
    }

    @Query("DELETE FROM DatabaseService WHERE id NOT IN (:ids)")
    suspend fun deleteNonExistentServices(ids: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<DatabaseService>)

    @Query("SELECT * FROM DatabaseService WHERE vin = :vin")
    fun getServices(vin: String): Flow<List<DatabaseService>>
}
