package at.sunilson.hvacschedule.presentation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setNavigationBarThemeColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.extensions.nightMode
import at.sunilson.scheduleCore.R
import at.sunilson.scheduleCore.presentation.ScheduleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HvacScheduleOverviewFragment : ScheduleFragment() {

    private val args by navArgs<HvacScheduleOverviewFragmentArgs>()
    override val vin: String
        get() = args.vin
    override val viewModel by viewModels<HvacScheduleViewModel>()
    override val isHvac: Boolean = true

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(requireContext().nightMode)
        setStatusBarColor(android.R.color.transparent)
        if (requireContext().nightMode) {
            setNavigationBarThemeColor(R.attr.colorSurface)
        } else {
            setNavigationBarColor(android.R.color.white)
        }
    }
}
