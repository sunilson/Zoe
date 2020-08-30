package at.sunilson.scheduleCore.data

import androidx.room.TypeConverter
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TypeConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(chargeType: ScheduleType): String {
        val adapter = moshi.adapter(ScheduleType::class.java)
        return adapter.toJson(chargeType)
    }

    @TypeConverter
    fun chargeTypeFromString(string: String): ScheduleType {
        val adapter = moshi.adapter(ScheduleType::class.java)
        return adapter.fromJson(string)!!
    }

    @TypeConverter
    fun toString(chargeDays: List<DatabaseScheduleDay>): String {
        val type = Types.newParameterizedType(List::class.java, DatabaseScheduleDay::class.java)
        val adapter = moshi.adapter<List<DatabaseScheduleDay>>(type)
        return adapter.toJson(chargeDays)
    }

    @TypeConverter
    fun chargeDayListFromString(string: String): List<DatabaseScheduleDay> {
        val type = Types.newParameterizedType(List::class.java, DatabaseScheduleDay::class.java)
        val adapter = moshi.adapter<List<DatabaseScheduleDay>>(type)
        return adapter.fromJson(string)!!
    }
}