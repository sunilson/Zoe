package at.sunilson.presentationcore.extensions

import android.animation.AnimatorInflater
import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import at.sunilson.presentationcore.R

fun Fragment.setupHeaderAnimation(
    container: View,
    list: RecyclerView?
): () -> Unit {
    // Set animator manually and directly jump to current state to prevent initial animation
    container.stateListAnimator = AnimatorInflater.loadStateListAnimator(
        requireContext(),
        R.animator.elevation_header_animator
    )
    container.jumpDrawablesToCurrentState()

    if(list == null) {
        container.isActivated = false
        return {}
    }

    val layoutListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        container.isActivated = list.canScrollVertically(-1)
    }
    list.addOnLayoutChangeListener(layoutListener)

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            container.isActivated = list.canScrollVertically(-1)
        }
    }
    list.addOnScrollListener(scrollListener)

    list.doOnLayout {
        container.isActivated = list.canScrollVertically(-1)
    }

    container.isActivated = list.canScrollVertically(-1)

    return {
        list.removeOnLayoutChangeListener(layoutListener)
        list.removeOnScrollListener(scrollListener)
    }
}

@ColorInt
fun Context.getThemeColor(@AttrRes res: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}