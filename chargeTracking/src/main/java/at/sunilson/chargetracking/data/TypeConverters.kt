package at.sunilson.chargetracking.data

import androidx.room.TypeConverter
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
    fun vehicleFromString(string: String): Vehicle.BatteryStatus {
        val adapter = moshi.adapter(Vehicle.BatteryStatus::class.java)
        return adapter.fromJson(string)!!
    }
}