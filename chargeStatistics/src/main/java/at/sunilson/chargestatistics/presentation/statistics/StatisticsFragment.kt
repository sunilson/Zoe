package at.sunilson.chargestatistics.presentation.statistics

import androidx.fragment.app.Fragment
import at.sunilson.chargestatistics.R
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation

internal class StatisticsFragment : Fragment(R.layout.statistics_fragment) {

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(null)
    }

}