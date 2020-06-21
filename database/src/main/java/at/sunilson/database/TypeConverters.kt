package at.sunilson.database

import androidx.room.TypeConverter
import at.sunilson.entities.Vehicle
import com.squareup.moshi.Moshi

class TypeConverters {

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