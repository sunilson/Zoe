package at.sunilson.vehicle.presentation.settingsDialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.presentationcore.extensions.withDefaultAnimations
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.DialogFragmentSettingsBinding
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SettingsDialogViewModel>()
    val binding by viewBinding(DialogFragmentSettingsBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setupHeaderAnimation(binding.handleContainer, binding.recyclerView)
        observeEvents()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                renderList(it.vehicles)
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    SettingsDialogEvent.VehicleSelected -> findNavController().navigate(
                        SettingsDialogFragmentDirections.reload()
                    )
                }
            }
        }
    }

    private fun renderList(vehicles: List<Vehicle>) {
        if (vehicles.isEmpty()) return

        binding.recyclerView.withModels {
            vehicles.forEach { vehicle ->

                settingsDialogButtons {
                    id("buttons")
                    settingsClicked {
                        findNavController().navigate(
                            Uri.parse("zoe://settings"),
                            NavOptions.Builder().withDefaultAnimations()
                        )
                    }
                    impressumClicked {
                        findNavController()
                            .navigate(
                                Uri.parse("zoe://impressum"),
                                NavOptions.Builder().withDefaultAnimations()
                            )
                    }
                }

                vehicleListItem {
                    id(vehicle.vin)
                    vehicle(vehicle)
                    onVehicleClick { viewModel.selectVehicle(it) }
                }
            }
        }
    }
}