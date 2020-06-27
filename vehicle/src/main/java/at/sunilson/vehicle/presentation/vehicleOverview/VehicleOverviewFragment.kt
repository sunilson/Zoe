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
import coil.api.load
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
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.show_settings_dialog)
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                Do exhaustive when (event) {
                    is ShowToast -> requireContext().showToast(event.message)
                    is ShowVehicleLocation -> findNavController().navigate(
                        VehicleOverviewFragmentDirections.showVehicleLocation(event.vin)
                    )
                    is ShowVehicleStatistics -> findNavController().navigate("https://zoe.app/statistics/${event.vin}".toUri())
                    is ShowVehicleDetails -> {
                        findNavController().navigate(
                            VehicleOverviewFragmentDirections.showVehicleDetails(
                                event.vin,
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
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.contentContainer.isRefreshing = state.loading
                if (state.selectedVehicle != null) {
                    renderVehicle(state.selectedVehicle)
                }
            }
        }
    }

    private fun renderVehicle(vehicle: Vehicle) {
        binding.vehicleName.text = "${vehicle.modelName} (${vehicle.batteryStatus.batteryLevel}%)"
        binding.progressBar.progress = vehicle.batteryStatus.batteryLevel.toFloat()
        binding.vehicleImage.load(vehicle.imageUrl)
        binding.vehicleImageSmall.load(vehicle.imageUrl)
        binding.recyclerView.withModels {
            batteryStatusWidget {
                id("batteryStatusWidget")
                batteryStatus(vehicle.batteryStatus)
            }

            climateControlWidget {
                id("climateControlWidget")
                startClimateControlClicked { viewModel.startClimateControl() }
            }

            buttonWidget {
                id("detailsButton")
                buttonText("Fahrzeug Details")
                onClick { viewModel.showVehicleDetails() }
            }

            buttonWidget {
                id("locationButton")
                buttonText("Fahrzeug Location")
                onClick { viewModel.showVehicleLocation() }
            }

            buttonWidget {
                id("statisticsButton")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton2")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton3")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton4")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton5")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton6")
                buttonText("Fahrzeug-Statistiken")
                onClick { viewModel.showVehicleStatistics() }
            }

            buttonWidget {
                id("statisticsButton7")
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