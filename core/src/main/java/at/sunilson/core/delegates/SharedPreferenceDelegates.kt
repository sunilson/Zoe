package at.sunilson.core.delegates

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class SharedPreferenceDelegate<T>(protected val sharedPreferences: SharedPreferences) :
    ReadWriteProperty<Any, T>

class BooleanPreferenceDelegate(
    sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean = false
) : SharedPreferenceDelegate<Boolean>(sharedPreferences) {
    override fun getValue(thisRef: Any, property: KProperty<*>) =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        sharedPreferences.edit { putBoolean(key, value) }
}