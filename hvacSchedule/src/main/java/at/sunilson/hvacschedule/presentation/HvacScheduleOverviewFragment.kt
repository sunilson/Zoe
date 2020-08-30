package at.sunilson.hvacschedule.presentation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import at.sunilson.scheduleCore.presentation.ScheduleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HvacScheduleOverviewFragment : ScheduleFragment() {

    private val args by navArgs<HvacScheduleOverviewFragmentArgs>()
    override val vin: String
        get() = args.vin
    override val viewModel by viewModels<HvacScheduleViewModel>()
    override val isHvac: Boolean = true
}