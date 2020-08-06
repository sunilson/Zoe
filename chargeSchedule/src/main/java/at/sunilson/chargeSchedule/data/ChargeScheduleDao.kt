package at.sunilson.chargeSchedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeSchedule

@Dao
internal interface ChargeScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChargeSchedules(schedules: List<DatabaseChargeSchedule>)

}