package at.sunilson.vehicleDetails.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import at.sunilson.core.Do
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.ktx.view.hideKeyboard
import at.sunilson.ktx.view.showKeyboard
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.vehicleDetails.R
import at.sunilson.vehicleDetails.data.VehicleDetailsService
import at.sunilson.vehicleDetails.databinding.FragmentVehicleDetailsBinding
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import at.sunilson.vehicleDetails.presentation.epoxy.models.detailInformation
import at.sunilson.vehicleDetails.presentation.epoxy.models.detailListEquipment
import at.sunilson.vehicleDetails.presentation.epoxy.models.detailListImage
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.airbnb.epoxy.EpoxyController
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(R.layout.fragment_vehicle_details) {

    @Inject
    internal lateinit var vehicleDetailsService: VehicleDetailsService

    @Inject
    internal lateinit var vehicleCoreRepository: VehicleCoreRepository

    private val args: VehicleDetailsFragmentArgs by navArgs()
    private val viewModel: VehicleDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentVehicleDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition()
        viewModel.refreshDetails(args.vin)
        viewModel.loadVehicle(args.vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeCommands()
        binding.searchButton.setOnClickListener {
            binding.headline.isVisible = false
            binding.searchInputLayout.isVisible = true
            binding.searchInput.requestFocus()
            binding.searchInput.showKeyboard()
            binding.searchButton.isVisible = false
            binding.cancelButton.isVisible = true
        }
        binding.cancelButton.setOnClickListener {
            binding.headline.isVisible = true
            binding.cancelButton.isVisible = false
            binding.searchInputLayout.isVisible = false
            binding.searchInput.setText("")
            binding.searchButton.isVisible = true
            binding.searchInput.clearFocus()
            binding.searchInput.hideKeyboard()
        }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.searchInput.doAfterTextChanged { viewModel.search(it.toString()) }
        Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.backButton)
        setupHeaderAnimation(binding.topContainer, binding.recyclerView, true)
    }

    override fun onPause() {
        super.onPause()
        binding.searchInput.hideKeyboard()
    }

    private fun observeCommands() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                when (it) {
                    is ScrollToPosition -> if (it.position == 0) {
                        binding.recyclerView.scrollTo(0, 0)
                    } else {
                        (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            it.position,
                            0
                        )
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.recyclerView.withModels {
                    renderDetailItems(it.details, it.searchedIndex)
                }
            }
        }
    }

    private fun EpoxyController.renderDetailItems(
        details: List<VehicleDetailsEntry>,
        searchedIndex: Int
    ) {
        var previousItem: VehicleDetailsEntry? = null
        details.forEachIndexed { index, detail ->
            Do exhaustive when (detail) {
                is VehicleDetailsEntry.Equipment -> detailListEquipment {
                    id(detail.code)
                    val castPreviousItem = previousItem as? VehicleDetailsEntry.Equipment
                    if (castPreviousItem == null || castPreviousItem.group != detail.group) {
                        title(detail.group)
                    }
                    item(detail)
                    marked(index == searchedIndex)
                }
                is VehicleDetailsEntry.Information -> detailInformation {
                    id(detail.code)
                    item(detail)
                    marked(index == searchedIndex)
                }
                is VehicleDetailsEntry.Image -> detailListImage {
                    id("vehicleImage")
                    imageUrl(detail.url)
                    imageLoaded { startPostponedEnterTransition() }
                    transitionName(
                        if (args.smallTransition) {
                            "vehicleImageSmall"
                        } else {
                            "vehicleImage"
                        }
                    )
                }
            }

            previousItem = detail
        }
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
    }
}