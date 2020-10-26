package at.sunilson.scheduleCore.data

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

interface ScheduleDao {

    @Transaction
    suspend fun insertAndDeleteSchedules(schedules: List<DatabaseSchedule>) {
        deleteNonExistentSchedules(schedules.map { it.id })
        insertSchedules(schedules)
    }

    @Query("DELETE FROM DatabaseSchedule WHERE id NOT IN (:ids)")
    suspend fun deleteNonExistentSchedules(ids: List<Int>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<DatabaseSchedule>)

    @Query("SELECT * FROM DatabaseSchedule WHERE vin = :vin")
    fun getAllSchedulesForVehicle(vin: String): Flow<List<DatabaseSchedule>>
}