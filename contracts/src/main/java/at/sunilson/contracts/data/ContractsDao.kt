package at.sunilson.contracts.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import at.sunilson.contracts.data.models.DatabaseContract
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ContractsDao {

    @Transaction
    suspend fun insertAndDeleteContracts(contracts: List<DatabaseContract>) {
        deleteNonExistentContracts(contracts.map { it.id })
        insertContracts(contracts)
    }

    @Query("DELETE FROM DatabaseContract WHERE id NOT IN (:ids)")
    suspend fun deleteNonExistentContracts(ids: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContracts(contracts: List<DatabaseContract>)

    @Query("SELECT * FROM DatabaseContract WHERE vin = :vin")
    fun getAllContracts(vin: String): Flow<List<DatabaseContract>>
}
