package at.sunilson.vehicle.presentation.vehicleDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleDetailsBinding
import at.sunilson.vehicle.presentation.vehicleDetails.epoxy.models.detailListItem
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.api.load
import com.airbnb.epoxy.EpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        binding.vehicleImage.transitionName = if (args.smallTransition) {
            "vehicleImageSmall"
        } else {
            "vehicleImage"
        }
        observeState()

        setupHeaderAnimation(binding.topContainer, binding.recyclerView, true)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                if (it.vehicle != null) {
                    launch {
                        binding
                            .vehicleImage
                            .load(it.vehicle.imageUrl)
                            .await()
                        startPostponedEnterTransition()
                    }

                    binding.recyclerView.withModels { renderDetailItems(it.vehicle) }
                }
            }
        }
    }

    private fun EpoxyController.renderDetailItems(vehicle: Vehicle) {
        detailListItem {
            id(0)
            title("VIN")
            body(vehicle.vin)
        }

        detailListItem {
            id(1)
            title("Kilometerstand")
            body("${vehicle.mileageKm} km")
        }

        detailListItem {
            id(2)
            title("Model")
            body(vehicle.modelName)
        }

        detailListItem {
            id(3)
            title("Batteriekapazität")
            body("${vehicle.batteryStatus.batteryCapacity} kWh")
        }

        detailListItem {
            id(4)
            title("Ladestand")
            body("${vehicle.batteryStatus.batteryLevel} % - ${vehicle.batteryStatus.availableEnery} kWh")
        }

        detailListItem {
            id(5)
            title("Restreichweite")
            body("${vehicle.batteryStatus.remainingRange} km")
        }

        detailListItem {
            id(6)
            title("Ladezustand")
            body(vehicle.batteryStatus.chargeState.name)
        }

        detailListItem {
            id(7)
            title("Verbleibende Ladedauer")
            body("${vehicle.batteryStatus.remainingChargeTime}")
        }

        detailListItem {
            id(8)
            title("Batterie Temperatur")
            body("${vehicle.batteryStatus.batteryTemperature} Grad")
        }
    }
}