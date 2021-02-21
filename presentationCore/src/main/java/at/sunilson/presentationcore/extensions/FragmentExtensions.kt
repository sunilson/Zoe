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
import at.sunilson.presentationcore.base.ProgressDialogFragment
import timber.log.Timber

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

    if (list == null) {
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

const val PROGRESS_DIALOG_FRAGMENT_TAG = "PROGRESS_DIALOG_FRAGMENT_TAG"

private val Fragment.existingProgressDialog
    get() = childFragmentManager.findFragmentByTag(PROGRESS_DIALOG_FRAGMENT_TAG) as? ProgressDialogFragment

fun Fragment.syncProgressDialogVisibility(show: Boolean = true) =
    if (show) showProgressDialog() else hideProgressDialog()

private fun Fragment.showNewProgressDialog() {
    ProgressDialogFragment().apply { isCancelable = false }
        .show(childFragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
    childFragmentManager.executePendingTransactions()
}

fun Fragment.showProgressDialog() {
    val existing = existingProgressDialog ?: return showNewProgressDialog()
    if (existing.isAdded) return
    // After some testing we assume this does not happen.
    // If it does we are save not showing the blocking dialog in these cases.
    Timber.tag(PROGRESS_DIALOG_FRAGMENT_TAG).w("Not added ProgressDialog is still on backstack.")
}

fun Fragment.hideProgressDialog() = existingProgressDialog?.run { if (isAdded) dismiss() }
