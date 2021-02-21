package at.sunilson.presentationcore.delegates

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ThemeColorDelegate(private val context: Context, @AttrRes private val attr: Int) :
    ReadOnlyProperty<Any?, @ColorInt Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
