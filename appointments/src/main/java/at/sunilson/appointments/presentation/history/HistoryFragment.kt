package at.sunilson.appointments.presentation.history

import androidx.fragment.app.Fragment
import at.sunilson.appointments.R
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation

internal class HistoryFragment : Fragment(R.layout.fragment_history) {

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(null)
    }

    companion object {
        fun newInstance(vin: String) = HistoryFragment()
    }
}