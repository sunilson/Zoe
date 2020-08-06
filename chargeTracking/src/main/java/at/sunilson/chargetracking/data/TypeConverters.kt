package at.sunilson.chargetracking.data

import androidx.room.TypeConverter
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi

internal class TypeConverters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(location: Location): String {
        val adapter = moshi.adapter(Location::class.java)
        return adapter.toJson(location)
    }

    @TypeConverter
    fun locationFromString(string: String?): Location? {
        return if (string == null) {
            null
        } else {
            val adapter = moshi.adapter(Location::class.java)
            adapter.fromJson(string)!!
        }
    }

    @TypeConverter
    fun toString(batteryStatus: Vehicle.BatteryStatus): String {
        val adapter = moshi.adapter(Vehicle.BatteryStatus::class.java)
        return adapter.toJson(batteryStatus)
    }

    @TypeConverter
    fun vehicleFromString(string: String): Vehicle.BatteryStatus {
        val adapter = moshi.adapter(Vehicle.BatteryStatus::class.java)
        return adapter.fromJson(string)!!
    }
}