package at.sunilson.chargestatistics.presentation.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ManageFragmentBinding
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ManageFragment : Fragment(R.layout.manage_fragment) {

    private val binding by viewBinding(ManageFragmentBinding::bind)
    private val viewModel by viewModels<ManageViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.recyclerView.withModels {
                    state.trackingInfos.forEach { vehicleChargeTrackingInfo ->
                        vehicleChargeTrackingInfo {
                            id(vehicleChargeTrackingInfo.vehicle.vin)
                            toggleTracking {
                                if (it) {
                                    viewModel.startTracking(vehicleChargeTrackingInfo.vehicle.vin)
                                } else {
                                    viewModel.stopTracking(vehicleChargeTrackingInfo.vehicle.vin)
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