package at.sunilson.vehiclecore.data

import androidx.room.TypeConverter
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi

internal class TypeConverters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(vehicle: at.sunilson.vehiclecore.domain.entities.Vehicle): String {
        val adapter = moshi.adapter(at.sunilson.vehiclecore.domain.entities.Vehicle::class.java)
        return adapter.toJson(vehicle)
    }

    @TypeConverter
    fun vehicleFromString(string: String): at.sunilson.vehiclecore.domain.entities.Vehicle {
        val adapter = moshi.adapter(at.sunilson.vehiclecore.domain.entities.Vehicle::class.java)
        return adapter.fromJson(string)!!
    }
}