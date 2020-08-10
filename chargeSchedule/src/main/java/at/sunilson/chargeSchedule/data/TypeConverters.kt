package at.sunilson.chargeSchedule.data

import androidx.room.TypeConverter
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeDay
import at.sunilson.chargeSchedule.domain.entities.ChargeType
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class TypeConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(chargeType: ChargeType): String {
        val adapter = moshi.adapter(ChargeType::class.java)
        return adapter.toJson(chargeType)
    }

    @TypeConverter
    fun chargeTypeFromString(string: String): ChargeType {
        val adapter = moshi.adapter(ChargeType::class.java)
        return adapter.fromJson(string)!!
    }

    @TypeConverter
    fun toString(chargeDays: List<DatabaseChargeDay>): String {
        val type = Types.newParameterizedType(List::class.java, DatabaseChargeDay::class.java)
        val adapter = moshi.adapter<List<DatabaseChargeDay>>(type)
        return adapter.toJson(chargeDays)
    }

    @TypeConverter
    fun chargeDayListFromString(string: String): List<DatabaseChargeDay> {
        val type = Types.newParameterizedType(List::class.java, DatabaseChargeDay::class.java)
        val adapter = moshi.adapter<List<DatabaseChargeDay>>(type)
        return adapter.fromJson(string)!!
    }
}