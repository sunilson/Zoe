package at.sunilson.chargestatistics.presentation.manage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ManageFragmentBinding
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class ManageFragment : Fragment(R.layout.manage_fragment) {

    private val binding by viewBinding(ManageFragmentBinding::bind)
    private val viewModel by viewModels<ManageViewModel>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(binding.recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.whitelistButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.container.stateFlow.collect { state ->
                binding.recyclerView.withModels {
                    state.trackingInfos.forEach { vehicleChargeTrackingInfo ->
                        vehicleChargeTrackingInfo {
                            id(vehicleChargeTrackingInfo.vehicle.vin)
                            toggleTracking {
                                if (it) {
                                    viewModel.startTrackingClicked(vehicleChargeTrackingInfo.vehicle.vin)
                                } else {
                                    viewModel.stopTrackingClicked(vehicleChargeTrackingInfo.vehicle.vin)
                                }
                            }
                            vehicleChargeTrackingInfo(vehicleChargeTrackingInfo)
                        }
                    }
                }
            }
        }
    }
}