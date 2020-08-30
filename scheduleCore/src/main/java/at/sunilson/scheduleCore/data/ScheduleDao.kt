package at.sunilson.scheduleCore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<DatabaseSchedule>)

    @Query("SELECT * FROM DatabaseSchedule WHERE vin = :vin")
    fun getAllSchedulesForVehicle(vin: String): Flow<List<DatabaseSchedule>>

}