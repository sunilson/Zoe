package at.sunilson.presentationcore.extensions

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes

inline fun View.applyStyledAttributes(
    attributeSet: AttributeSet?,
    @StyleableRes styleable: IntArray,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    block: TypedArray.() -> Unit
) {
    context.theme.obtainStyledAttributes(attributeSet, styleable, defStyleAttr, defStyleRes).apply {
        try {
            this.block()
        } finally {
            recycle()
        }
    }
}
