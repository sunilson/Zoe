package at.sunilson.chargeSchedule.presentation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import at.sunilson.scheduleCore.presentation.ScheduleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChargeScheduleOverviewFragment : ScheduleFragment() {
    private val args by navArgs<ChargeScheduleOverviewFragmentArgs>()
    override val viewModel by viewModels<ChargeScheduleOverviewViewModel>()
    override val vin: String
        get() = args.vin
}