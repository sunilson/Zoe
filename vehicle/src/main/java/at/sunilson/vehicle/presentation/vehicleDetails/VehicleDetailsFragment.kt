package at.sunilson.vehicle.presentation.vehicleDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleDetailsBinding
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(R.layout.fragment_vehicle_details) {

    private val args: VehicleDetailsFragmentArgs by navArgs()
    private val viewModel: VehicleDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentVehicleDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        viewModel.loadVehicle(args.vehicleVin)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                if (it.vehicle != null) {
                    binding.vehicleImage.load(it.vehicle?.imageUrl)
                    startPostponedEnterTransition()
                }
            }
        }
    }
}