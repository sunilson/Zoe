package at.sunilson.contracts.data

import androidx.room.Database
import androidx.room.RoomDatabase
import at.sunilson.contracts.data.models.DatabaseContract

@Database(
    entities = [DatabaseContract::class],
    version = 1
)
internal abstract class ContractsDatabase : RoomDatabase() {
    abstract fun contractsDao(): ContractsDao
}
