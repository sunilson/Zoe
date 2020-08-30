package at.sunilson.appointments.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class TypeConverters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toString(list: List<Int>): String {
        val type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val adapter = moshi.adapter<List<Int>>(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        val type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val adapter = moshi.adapter<List<Int>>(type)
        return adapter.fromJson(string)!!
    }
}