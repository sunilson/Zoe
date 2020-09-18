package at.sunilson.chargetracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.sunilson.chargetracking.data.models.ChargeTrackingPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ChargeTrackingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertChargeTrackingPoint(chargeTrackingPoint: ChargeTrackingPoint)

    @Query("SELECT * FROM CHARGETRACKINGPOINT WHERE vehicleId = :vin")
    abstract fun getAllChargeTrackingPoints(vin: String): Flow<List<ChargeTrackingPoint>>

    @Query("SELECT * FROM CHARGETRACKINGPOINT WHERE vehicleId = :vin ORDER BY timestamp DESC LIMIT 1")
    abstract suspend fun getLatestChargeTrackingPoint(vin: String): ChargeTrackingPoint?
}