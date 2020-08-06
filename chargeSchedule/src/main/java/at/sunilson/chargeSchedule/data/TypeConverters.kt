package at.sunilson.chargeSchedule.data

import androidx.room.TypeConverter
import at.sunilson.chargeSchedule.domain.entities.ChargeDay
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi

internal class TypeConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(chargeDay: ChargeDay): String {
        val adapter = moshi.adapter(ChargeDay::class.java)
        return adapter.toJson(chargeDay)
    }

    @TypeConverter
    fun vehicleFromString(string: String): ChargeDay {
        val adapter = moshi.adapter(ChargeDay::class.java)
        return adapter.fromJson(string)!!
    }
}