package at.sunilson.vehicle.presentation.vehicleDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleDetailsBinding
import at.sunilson.vehicle.presentation.vehicleDetails.epoxy.models.detailListImage
import at.sunilson.vehicle.presentation.vehicleDetails.epoxy.models.detailListItem
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.airbnb.epoxy.EpoxyController
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(R.layout.fragment_vehicle_details) {

    private val args: VehicleDetailsFragmentArgs by navArgs()
    private val viewModel: VehicleDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentVehicleDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition()

        viewModel.loadVehicle(args.vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.backButton)
        setupHeaderAnimation(binding.topContainer, binding.recyclerView, true)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                if (it.vehicle != null) {
                    binding.recyclerView.withModels { renderDetailItems(it.vehicle) }
                }
            }
        }
    }

    private fun EpoxyController.renderDetailItems(vehicle: Vehicle) {
        detailListImage {
            id("vehicleImage")
            imageUrl(vehicle.imageUrl)
            imageLoaded { startPostponedEnterTransition() }
            transitionName(
                if (args.smallTransition) {
                    "vehicleImageSmall"
                } else {
                    "vehicleImage"
                }
            )
        }

        detailListItem {
            id(0)
            title("VIN")
            body(vehicle.vin)
        }

        detailListItem {
            id("Kilometerstand")
            title("Kilometerstand")
            body("${vehicle.mileageKm} km")
        }

        detailListItem {
            id("Marke")
            title("Marke")
            body(vehicle.brand)
        }

        detailListItem {
            id("Model")
            title("Model")
            body(vehicle.modelName)
        }

        detailListItem {
            id("Version")
            title("Version")
            body(vehicle.modelVersion)
        }

        detailListItem {
            id("Batterie")
            title("Batterie")
            body(vehicle.batteryLabel)
        }

        detailListItem {
            id("Batteriekapazität")
            title("Batteriekapazität")
            body("${vehicle.batteryStatus.batteryCapacity} kWh")
        }

        detailListItem {
            id("Ladestand")
            title("Ladestand")
            body("${vehicle.batteryStatus.batteryLevel} % - ${vehicle.batteryStatus.availableEnery} kWh")
        }

        detailListItem {
            id("Restreichweite")
            title("Restreichweite")
            body("${vehicle.batteryStatus.remainingRange} km")
        }

        detailListItem {
            id("Ladezustand")
            title("Ladezustand")
            body(vehicle.batteryStatus.chargeState.name)
        }

        detailListItem {
            id("Verbleibende Ladedauer")
            title("Verbleibende Ladedauer")
            body("${vehicle.batteryStatus.remainingChargeTime}")
        }

        detailListItem {
            id("Batterie Temperatur")
            title("Batterie Temperatur")
            body("${vehicle.batteryStatus.batteryTemperature} Grad")
        }

        detailListItem {
            id("Years of Maintenance")
            title("Years of Maintenance")
            body(vehicle.yearsOfMaintainance.toString())
        }

        detailListItem {
            id("Konnektivität")
            title("Konnektivität")
            body(vehicle.connectivityTechnology)
        }

        detailListItem {
            id("AnnualMileage")
            title("Jährliche Kilometer")
            body("${vehicle.annualMileage} km")
        }
    }

    override fun onResume() {
        super.onResume()

        useLightStatusBarIcons(false)
    }
}