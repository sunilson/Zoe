package at.sunilson.presentationcore.extensions

import android.animation.AnimatorInflater
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import at.sunilson.presentationcore.R

fun Fragment.setupHeaderAnimation(
    container: View?,
    list: View?,
    elevationOnly: Boolean = false
) {
    // Set animator manually and directly jump to current state to prevent initial animation
    val animator = if (elevationOnly) {
        R.animator.elevation_header_animator
    } else {
        R.animator.white_header_animator
    }
    container?.stateListAnimator = AnimatorInflater.loadStateListAnimator(
        requireContext(),
        animator
    )
    container?.jumpDrawablesToCurrentState()

    list?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        container?.isActivated = list.canScrollVertically(-1)
    }

    if (list is RecyclerView) {
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                container?.isActivated = list.canScrollVertically(-1)
            }
        })
    } else {
        list?.setOnScrollChangeListener { _, _, _, _, _ ->
            container?.isActivated = list.canScrollVertically(-1)
        }
    }

    list?.doOnLayout {
        container?.isActivated = list.canScrollVertically(-1)
    }
}

fun Fragment.updateHeaderAnimationWithViewpager(activePosition: Int, container: View?) {
    val fragment = childFragmentManager.findFragmentByTag("f$activePosition") ?: return
    val recyclerview = (fragment.view as? ViewGroup)
        ?.children
        ?.filterIsInstance<RecyclerView>()
        ?.firstOrNull()

    if (recyclerview != null) {
        setupHeaderAnimation(container, recyclerview, true)
    } else {
        container?.isActivated = false
    }
}

@ColorInt
fun Context.getThemeColor(@AttrRes res: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}