package at.sunilson.presentationcore.extensions

import android.animation.AnimatorInflater
import android.view.View
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
}
