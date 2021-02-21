package at.sunilson.scheduleCore.data

import androidx.room.TypeConverter
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import java.time.LocalTime

class TypeConverters {
    private val moshi = Moshi
        .Builder()
        .add(LocalTimeAdapter())
        .build()

    class LocalTimeAdapter {
        @FromJson
        fun fromJson(string: String): LocalTime {
            return LocalTime.parse(string)
        }

        @ToJson
        fun toJson(localTime: LocalTime): String {
            return localTime.toString()
        }
    }

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
    fun toString(chargeDays: List<ScheduleDay>): String {
        val type = Types.newParameterizedType(List::class.java, ScheduleDay::class.java)
        val adapter = moshi.adapter<List<ScheduleDay>>(type)
        return adapter.toJson(chargeDays)
    }

    @TypeConverter
    fun chargeDayListFromString(string: String): List<ScheduleDay> {
        val type = Types.newParameterizedType(List::class.java, ScheduleDay::class.java)
        val adapter = moshi.adapter<List<ScheduleDay>>(type)
        return adapter.fromJson(string)!!
    }
}
