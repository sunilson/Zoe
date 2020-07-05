package at.sunilson.presentationcore.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import at.sunilson.presentationcore.R
import at.sunilson.presentationcore.extensions.applyStyledAttributes

class CurtainView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var radius: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var color: Int? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        applyStyledAttributes(attrs, R.styleable.CurtainView) {
            color = getColor(R.styleable.CurtainView_color, Color.RED)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            clipPath(getCutout(width.toFloat(), height.toFloat(), radius))
            color?.let { drawColor(it) }
        }
        super.onDraw(canvas)
    }

    private fun getCutout(width: Float, height: Float, radius: Float): Path {
        val path = Path()
        path.moveTo(0F, height)
        path.addArc(botLeftQuarter(height, radius), 180F, 90F)
        path.rLineTo(width - 2 * radius, 0F)
        path.addArc(botRightQuarter(width, height, radius), 270F, 90F)
        path.lineTo(width, 0F)
        path.lineTo(0F, 0F)
        path.lineTo(0F, height)
        return path
    }

    private fun botLeftQuarter(height: Float, radius: Float): RectF {
        return RectF(0F, height - radius, (2 * radius), height + radius)
    }

    private fun botRightQuarter(width: Float, height: Float, radius: Float): RectF {
        return RectF(width - (2 * radius), height - radius, width, height + radius)
    }

    init {
        applyStyledAttributes(attrs, R.styleable.CurtainView) {
            radius = getDimensionPixelSize(R.styleable.CurtainView_curtainRadius, 0).toFloat()
        }
    }
}
