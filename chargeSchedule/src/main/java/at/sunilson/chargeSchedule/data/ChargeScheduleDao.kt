package at.sunilson.chargeSchedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.sunilson.scheduleCore.data.DatabaseSchedule
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ChargeScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChargeSchedules(schedules: List<DatabaseSchedule>)

    @Query("SELECT * FROM DatabaseSchedule WHERE vin = :vin")
    fun getAllChargeSchedulesForVehicle(vin: String): Flow<List<DatabaseSchedule>>

}