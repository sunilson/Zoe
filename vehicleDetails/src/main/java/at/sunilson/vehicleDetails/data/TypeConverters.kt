package at.sunilson.vehicleDetails.data

import androidx.room.TypeConverter
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TypeConverters {
    private val moshi = Moshi
        .Builder()
        .build()

    @TypeConverter
    fun toString(detailEntries: List<VehicleDetailsEntry>): String {
        val type = Types.newParameterizedType(List::class.java, VehicleDetailsEntry::class.java)
        val adapter = moshi.adapter<List<VehicleDetailsEntry>>(type)
        return adapter.toJson(detailEntries)
    }

    @TypeConverter
    fun chargeDayListFromString(string: String): List<VehicleDetailsEntry> {
        val type = Types.newParameterizedType(List::class.java, VehicleDetailsEntry::class.java)
        val adapter = moshi.adapter<List<VehicleDetailsEntry>>(type)
        return adapter.fromJson(string)!!
    }
}
