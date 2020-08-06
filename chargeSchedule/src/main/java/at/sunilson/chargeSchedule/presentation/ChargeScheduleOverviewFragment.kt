package at.sunilson.chargeSchedule.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import at.sunilson.chargeSchedule.R
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChargeScheduleOverviewFragment : Fragment(R.layout.fragment_charge_schedules_overview) {

    private val args by navArgs<ChargeScheduleOverviewFragmentArgs>()
    private val viewModel by viewModels<ChargeScheduleOverviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshSchedules(args.vin)
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
    }
}