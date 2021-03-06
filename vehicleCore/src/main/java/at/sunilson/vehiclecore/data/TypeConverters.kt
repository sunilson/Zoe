package at.sunilson.vehiclecore.data

import androidx.room.TypeConverter
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi

internal class TypeConverters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(batteryStatus: Vehicle.BatteryStatus): String {
        val adapter = moshi.adapter(Vehicle.BatteryStatus::class.java)
        return adapter.toJson(batteryStatus)
    }

    @TypeConverter
    fun batteryStatusFromString(string: String): Vehicle.BatteryStatus {
        val adapter = moshi.adapter(Vehicle.BatteryStatus::class.java)
        return adapter.fromJson(string)!!
    }

    @TypeConverter
    fun toString(vehicle: Vehicle): String {
        val adapter = moshi.adapter(Vehicle::class.java)
        return adapter.toJson(vehicle)
    }

    @TypeConverter
    fun vehicleFromString(string: String): Vehicle {
        val adapter = moshi.adapter(Vehicle::class.java)
        return adapter.fromJson(string)!!
    }

    @TypeConverter
    fun toString(location: Location?): String? {
        if (location == null) return null

        val adapter = moshi.adapter(Location::class.java)
        return adapter.toJson(location)
    }

    @TypeConverter
    fun locationFromString(string: String?): Location? {
        if (string.isNullOrEmpty()) return null

        val adapter = moshi.adapter(Location::class.java)
        return adapter.fromJson(string)
    }
}
