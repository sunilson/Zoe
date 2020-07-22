package at.sunilson.chargetracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import at.sunilson.chargetracking.data.models.ChargeTrackingPoint

@Dao
internal abstract class ChargeTrackingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract suspend fun insertChargeTrackingPoint(chargeTrackingPoint: ChargeTrackingPoint)
}