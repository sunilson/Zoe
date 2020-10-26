package at.sunilson.vehiclecore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import at.sunilson.vehiclecore.data.models.DatabaseVehicle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class VehicleDao {

    @Query("SELECT * FROM DatabaseVehicle WHERE vin = :id")
    abstract fun getVehicle(id: String): Flow<DatabaseVehicle?>

    @Query("SELECT * FROM DatabaseVehicle WHERE vin = :id")
    abstract suspend fun getVehicleOnce(id: String): DatabaseVehicle?

    @Query("SELECT * FROM DatabaseVehicle")
    abstract fun getAllVehicles(): Flow<List<DatabaseVehicle>>

    @Query("UPDATE DatabaseVehicle SET vehicleannualMileage = :annualMileage WHERE vin = :vin")
    abstract suspend fun updateAnnualMileage(vin: String, annualMileage: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertVehicles(databaseVehicles: List<DatabaseVehicle>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun updateVehicles(databaseVehicles: List<DatabaseVehicle>)

    @Query("DELETE FROM DatabaseVehicle WHERE vin NOT IN (:vins)")
    protected abstract suspend fun deleteNonExistentVehicles(vins: List<String>)

    @Transaction
    open suspend fun upsertVehicles(databaseVehicles: List<DatabaseVehicle>) {
        deleteNonExistentVehicles(databaseVehicles.map { it.vin })
        insertVehicles(databaseVehicles)
        updateVehicles(databaseVehicles)
    }
}