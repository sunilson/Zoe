package at.sunilson.vehiclecore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import at.sunilson.vehiclecore.data.models.DatabaseLocation
import at.sunilson.vehiclecore.data.models.DatabaseVehicle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class VehicleDao {

    @Query("SELECT * FROM DatabaseVehicle WHERE vin = :id")
    abstract fun getVehicle(id: String): Flow<DatabaseVehicle?>

    @Query("SELECT * FROM DatabaseVehicle")
    abstract fun getAllVehicles(): Flow<List<DatabaseVehicle>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertVehicles(databaseVehicles: List<DatabaseVehicle>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun updateVehicles(databaseVehicles: List<DatabaseVehicle>)

    @Query("SELECT * FROM DatabaseLocation WHERE vin = :vin")
    abstract fun getVehicleLocation(vin: String): Flow<DatabaseLocation?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVehicleLocation(databaseLocation: DatabaseLocation)

    @Transaction
    open suspend fun upsertVehicles(databaseVehicles: List<DatabaseVehicle>) {
        insertVehicles(databaseVehicles)
        updateVehicles(databaseVehicles)
    }
}