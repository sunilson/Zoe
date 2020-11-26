package at.sunilson.presentationcore.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.core.view.doOnNextLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MotionLayoutSavedState @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : MotionLayout(context, attributeSet) {

    /**
     * This class is used to restore state when the view is recreated
     */
    private class SavedState : BaseSavedState {
        var progress: Float = 0f

        constructor(parcel: Parcel) : super(parcel) {
            progress = parcel.readFloat()
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(progress)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = SavedState(super.onSaveInstanceState())
        state.progress = progress
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            doOnNextLayout { progress = state.progress }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        if (target !is SwipeRefreshLayout) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val scrollView = target.children.firstOrNull { it is RecyclerView }

        if (scrollView !is RecyclerView) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val canScrollVertically = scrollView.canScrollVertically(-1)
        if (dy < 0 && canScrollVertically) {
            // don't start motionLayout transition
            return;
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }
}
