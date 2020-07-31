package at.sunilson.vehiclecore.data

import androidx.room.TypeConverter
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi

internal class TypeConverters {

    private val moshi = Moshi.Builder().build()

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
}