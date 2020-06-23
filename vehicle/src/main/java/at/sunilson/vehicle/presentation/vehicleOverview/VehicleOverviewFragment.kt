package at.sunilson.vehicle.presentation.vehicleOverview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
import at.sunilson.entities.Vehicle
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
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
            viewModel.showVehicleDetails()
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
                    is ShowVehicleDetails -> {
                        findNavController().navigate(
                            VehicleOverviewFragmentDirections.showVehicleDetails(event.vin),
                            FragmentNavigatorExtras(binding.vehicleImage to "vehicleImage")
                        )
                    }
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
        binding.vehicleBatteryPlugged.text = if (vehicle.batteryStatus.pluggedIn) {
            getString(R.string.plugged_in)
        } else {
            getString(R.string.not_plugged_in)
        }
        binding.vehicleMileage.text = getString(R.string.mileage, vehicle.mileageKm.toString())
        binding.vehicleName.text = vehicle.modelName
        binding.vehicleVin.text = vehicle.vin
        binding.batterStatusBar.progress = vehicle.batteryStatus.batteryLevel
        binding.vehicleImage.load(vehicle.imageUrl)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshVehicles()
        setStatusBarColor(android.R.color.white)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
    }
}