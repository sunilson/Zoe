package at.sunilson.presentationcore.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import at.sunilson.presentationcore.R
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0
) : View(context, attributeSet, style) {

    /**
     * The width of the stroke of the displayed progress
     */
    var strokeWidth: Float = 1f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    /**
     * The width of the background stroke
     */
    var backgroundStrokeWidth: Float = 1f
        set(value) {
            field = value
            backgroundPaint.strokeWidth = value
        }


    /**
     * Progress of the [CircularProgressBar] in percent. Changes are animated
     */
    var progress: Float = 0f
        set(value) {
            if (value == field) return

            field = value
            val newAngle = 3.6f * value

            if (currentAnimator != null && currentAnimator?.isRunning == true) currentAnimator?.cancel()
            currentAnimator = ValueAnimator.ofFloat(angle, newAngle).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 1000
                addUpdateListener {
                    angle = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }

    /**
     * If true, a thin lined circle will be drawn under the progress line
     */
    var hasBackgroundRing: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var strokeColor: Int = Color.WHITE
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    @ColorInt
    var backgroundStrokeColor: Int = Color.WHITE
        set(value) {
            Timber.d("Setting circular progress background stroke color to $value")
            field = value
            backgroundPaint.color = value
            invalidate()
        }

    private var currentAnimator: ValueAnimator? = null
    private var angle = 18f
    private val rect = RectF()

    //Paint used for main progress stroke
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(android.R.color.black)
        setStyle(Paint.Style.STROKE)
        strokeCap = Paint.Cap.ROUND
    }

    //Paint used for background circle
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(android.R.color.black).apply { alpha = 100 }
        setStyle(Paint.Style.STROKE)
        strokeWidth = backgroundStrokeWidth
    }


    /**
     * Recalculate circle width and height when the views size changes
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val width = min(w, h)

        val offsetX = if (w == width) 0 else (w - width) / 2
        val offsetY = if (h == width) 0 else (h - width) / 2

        val maxWidth = max(strokeWidth, backgroundStrokeWidth)

        rect.set(
            0f + maxWidth / 2 + offsetX,
            0f + maxWidth / 2 + offsetY,
            width.toFloat() - maxWidth / 2 + offsetX,
            width.toFloat() - maxWidth / 2 + offsetY
        )
    }

    /**
     * Draw the background and foreground strokes
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (hasBackgroundRing) canvas?.drawOval(rect, backgroundPaint)
        canvas?.drawArc(rect, 270f, angle, false, paint)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        Timber.d("Restored circular progress bar")
    }

    init {
        //Get initial attributes
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CircularProgressBar,
            0, 0
        ).apply {
            try {
                strokeWidth = getFloat(R.styleable.CircularProgressBar_foregroundStrokeWidth, 50f)
                backgroundStrokeWidth =
                    getFloat(R.styleable.CircularProgressBar_backgroundStrokeWidth, 50f)
                progress = getFloat(R.styleable.CircularProgressBar_progress, 0f)
                strokeColor = getColor(R.styleable.CircularProgressBar_strokeColor, Color.WHITE)
                backgroundStrokeColor =
                    getColor(R.styleable.CircularProgressBar_backgroundStrokeColor, Color.WHITE)
                hasBackgroundRing =
                    getBoolean(R.styleable.CircularProgressBar_hasBackgroundRing, true)
            } finally {
                recycle()
            }
        }
    }
}