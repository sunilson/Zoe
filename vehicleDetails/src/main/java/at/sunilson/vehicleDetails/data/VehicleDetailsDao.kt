package at.sunilson.vehicleDetails.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.sunilson.vehicleDetails.data.models.DatabaseVehicleDetails
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VehicleDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(entries: DatabaseVehicleDetails)

    @Query("SELECT * FROM DatabaseVehicleDetails WHERE vin = :vin")
    fun getAllVehicleDetails(vin: String): Flow<DatabaseVehicleDetails?>

}