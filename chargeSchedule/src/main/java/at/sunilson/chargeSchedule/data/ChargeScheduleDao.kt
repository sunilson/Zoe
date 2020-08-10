package at.sunilson.chargeSchedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeSchedule
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ChargeScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChargeSchedules(schedules: List<DatabaseChargeSchedule>)

    @Query("SELECT * FROM DatabaseChargeSchedule WHERE vin = :vin")
    fun getAllChargeSchedulesForVehicle(vin: String): Flow<List<DatabaseChargeSchedule>>

}