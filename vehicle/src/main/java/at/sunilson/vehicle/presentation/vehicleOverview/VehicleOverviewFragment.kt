package at.sunilson.vehicle.presentation.vehicleOverview

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
import at.sunilson.entities.Location
import at.sunilson.entities.Vehicle
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleOverviewBinding
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.batteryStatusWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.buttonWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.climateControlWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleDetailsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleLocationWidget
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleOverviewFragment : Fragment(R.layout.fragment_vehicle_overview) {
    private val binding by viewBinding(FragmentVehicleOverviewBinding::bind)
    private val viewModel by viewModels<VehicleOverviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshVehicles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupSwipeRefreshLayout()
        observeState()
        observeEvents()
        setupInsets()
        setupHeaderAnimation(binding.cutView, binding.recyclerView, true)
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun setupInsets() {
        requireView().doOnApplyWindowInsets { v, insets, initialState ->
            binding.motionLayout.updatePadding(top = insets.systemWindowInsetTop + initialState.paddings.top)
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.contentContainer.setOnRefreshListener {
            viewModel.refreshVehicles()
        }
    }

    private fun setupClickListeners() {
        binding.vehicleImage.setOnClickListener {
            viewModel.showVehicleDetails()
        }

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.show_settings_dialog)
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                Do exhaustive when (event) {
                    is ShowToast -> requireContext().showToast(event.message)
                    is ShowVehicleDetails -> showVehicleDetails(event.vin)
                    is ShowVehicleStatistics -> findNavController().navigate("https://zoe.app/statistics/${event.vin}".toUri())
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.contentContainer.isRefreshing = state.loading
                if (state.selectedVehicle != null) {
                    renderVehicle(state.selectedVehicle, state.vehicleLocation)
                }
            }
        }
    }

    private fun showVehicleLocation(vin: String) {
        exitTransition = Hold()
        findNavController().navigate(
            VehicleOverviewFragmentDirections.showVehicleLocation(vin),
            FragmentNavigatorExtras(requireView().findViewById<MaterialCardView>(R.id.location_widget) to "location")
        )
    }

    private fun showVehicleDetails(vin: String) {
        exitTransition = null
        findNavController().navigate(
            VehicleOverviewFragmentDirections.showVehicleDetails(
                vin,
                binding.motionLayout.progress != 0f
            ),
            FragmentNavigatorExtras(
                if (binding.motionLayout.progress == 0f) {
                    binding.vehicleImage to "vehicleImage"
                } else {
                    binding.vehicleImageSmall to "vehicleImageSmall"
                }
            )
        )
    }

    private fun renderVehicle(vehicle: Vehicle, location: Location?) {
        binding.vehicleSubtitle.text =
            if (vehicle.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGING) {
                "Am laden mit ${vehicle.batteryStatus.remainingChargeTime} Minuten verbleibend"
            } else {
                "Derzeit nicht am laden"
            }
        binding.vehicleTitle.text =
            "${vehicle.batteryStatus.batteryLevel}% (${vehicle.batteryStatus.remainingRange} Km)"
        binding.progressBar.progress = vehicle.batteryStatus.batteryLevel.toFloat()
        binding.vehicleImage.load(vehicle.imageUrl)
        binding.vehicleImageSmall.load(vehicle.imageUrl)
        binding.recyclerView.withModels {

            vehicleDetailsWidget {
                id("vehicleDetailsWidget")
                vehicle(vehicle)
                onButtonClick(this@VehicleOverviewFragment::showVehicleDetails)
            }

            batteryStatusWidget {
                id("batteryStatusWidget")
                batteryStatus(vehicle.batteryStatus)
            }

            climateControlWidget {
                id("climateControlWidget")
                planClimateControlClicked { TODO() }
                startClimateControlClicked { viewModel.startClimateControl() }
            }

            vehicleLocationWidget {
                id("vehicleLocationWidget")
                vehicle(vehicle)
                location(location)
                onMapClick { showVehicleLocation(it) }
            }

            buttonWidget {
                id("statisticsButton")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(true)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }
}