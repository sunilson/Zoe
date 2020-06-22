package at.sunilson.vehicle.presentation.vehicleOverview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
import at.sunilson.entities.Vehicle
import at.sunilson.ktx.context.showToast
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleOverviewBinding
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleOverviewFragment : Fragment(R.layout.fragment_vehicle_overview) {
    private val binding by viewBinding(FragmentVehicleOverviewBinding::bind)
    private val viewModel by viewModels<VehicleOverviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupRefreshLayout()
        observeState()
        observeEvents()
    }

    private fun setupClickListeners() {
        binding.startHvacButton.setOnClickListener {
            viewModel.startClimateControl()
        }

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.show_settings_dialog)
        }

        binding.vehicleDetailsButton.setOnClickListener {
            findNavController().navigate(R.id.show_vehicle_details)
        }
    }

    private fun setupRefreshLayout() {
        //TODO
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                Do exhaustive when (event) {
                    is ShowToast -> requireContext().showToast(event.message)
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.selectedVehicle != null) {
                    renderVehicle(state.selectedVehicle)
                }
            }
        }
    }

    private fun renderVehicle(vehicle: Vehicle) {
        binding.vehicleMileage.text = "Kilometerstand ${vehicle.mileageKm} Kilometer"
        binding.vehicleName.text = vehicle.modelName
        binding.vehicleVin.text = vehicle.vin
        binding.batterStatusBar.progress = vehicle.batteryStatus.batteryLevel
        binding.vehicleImage.load(vehicle.imageUrl)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshVehicles()
    }
}