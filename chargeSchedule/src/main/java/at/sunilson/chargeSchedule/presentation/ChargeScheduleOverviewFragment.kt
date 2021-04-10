package at.sunilson.chargeSchedule.presentation

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
internal class ChargeScheduleOverviewFragment : ScheduleFragment() {
    private val args by navArgs<ChargeScheduleOverviewFragmentArgs>()
    override val viewModel by viewModels<ChargeScheduleOverviewViewModel>()
    override val vin: String
        get() = args.vin
}
