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
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.drawBelowNavigationBar
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setNavigationBarThemeColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.ktx.view.hideKeyboard
import at.sunilson.ktx.view.showKeyboard
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.nightMode
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
import dev.chrisbanes.insetter.windowInsetTypesOf
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
internal class VehicleDetailsFragment : Fragment(R.layout.fragment_vehicle_details) {

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
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        postponeEnterTransition()
        viewModel.refreshDetailsRequested(args.vin)
        viewModel.viewCreated(args.vin)
    }

    override fun onPause() {
        super.onPause()
        binding.searchInput.hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeSideEffects()

        setupInsets()
        binding.searchButton.setOnClickListener { viewModel.searchButtonClicked() }
        binding.cancelButton.setOnClickListener { viewModel.searchButtonClicked() }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.searchInput.doAfterTextChanged { viewModel.searchQueryEntered(it.toString()) }
        binding.refreshLayout.setOnRefreshListener { viewModel.refreshDetailsRequested(args.vin) }
        setupHeaderAnimation(binding.topContainer, binding.recyclerView)
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect {
                Do exhaustive when (it) {
                    is VehicleDetailsSideEffects.ScrollToPosition -> if (it.position == 0) {
                        binding.recyclerView.scrollTo(0, 0)
                    } else {
                        (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            it.position,
                            0
                        )
                    }
                    is VehicleDetailsSideEffects.RefreshFailed -> requireContext().showToast(R.string.request_failed)
                }
            }
        }
    }

    private fun observeState() {
        var previousShowSearch = false
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.stateFlow.collect { state ->

                binding.searchInputLayout.isVisible = state.showSearch
                binding.cancelButton.isVisible = state.showSearch
                binding.searchButton.isVisible = !state.showSearch
                binding.headline.isVisible = !state.showSearch

                if (state.showSearch && !previousShowSearch) {
                    binding.searchInput.requestFocus()
                    binding.searchInput.showKeyboard()
                } else {
                    binding.searchInput.clearFocus()
                    binding.searchInput.hideKeyboard()
                }
                previousShowSearch = state.showSearch

                binding.refreshLayout.isRefreshing = state.loading
                binding.recyclerView.withModels {
                    renderDetailItems(state.details, state.searchedIndex)
                }
            }
        }
    }

    private fun setupInsets() {
        Insetter
            .builder()
            .padding(windowInsetTypesOf(statusBars = true))
            .applyToView(binding.topContainer)

        Insetter
            .builder()
            .padding(windowInsetTypesOf(statusBars = true))
            .applyToView(binding.refreshLayout)
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
                    transitionName(
                        if (args.smallTransition) {
                            "vehicleImageSmall"
                        } else {
                            "vehicleImage"
                        }
                    )
                    imageUrl(detail.url)
                    imageLoaded { startPostponedEnterTransition() }
                }
            }

            previousItem = detail
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(android.R.color.transparent)
        if (requireContext().nightMode) {
            setNavigationBarThemeColor(R.attr.colorSurface)
        } else {
            setNavigationBarColor(android.R.color.white)
        }
        useLightStatusBarIcons(requireContext().nightMode)
        drawBelowStatusBar(true)
        drawBelowNavigationBar(false)
    }
}
