package at.sunilson.vehiclecore.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import at.sunilson.vehiclecore.domain.entities.Vehicle

@Entity
data class DatabaseVehicle(
    @PrimaryKey val vin: String,
    @Embedded(prefix = "vehicle") val vehicle: Vehicle
)

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE DatabaseVehicle ADD COLUMN vehicleannualMileage INTEGER NOT NULL DEFAULT 0")
    }
}