package at.sunilson.vehicleDetails.presentation

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
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
import at.sunilson.ktx.fragment.setStatusBarColor
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
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition()
        viewModel.refreshDetails(args.vin)
        viewModel.loadVehicle(args.vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeCommands()

        setupMotionLayout()
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.searchInput.doAfterTextChanged { viewModel.search(it.toString()) }
        binding.refreshLayout.setOnRefreshListener { viewModel.refreshDetails(args.vin) }
        setupHeaderAnimation(binding.topContainer, binding.recyclerView)
    }

    private fun setupMotionLayout() {
        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.topContainer)
        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.BOTTOM)
            .applyToView(binding.refreshLayout)

        binding.topContainer.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(
                p0: MotionLayout?,
                start: Int,
                end: Int,
                progress: Float
            ) {
                binding.searchInput.isEnabled = progress >= 0.5f
                binding.searchButton.setImageResource(if (progress >= 0.5f) R.drawable.ic_baseline_close_24 else R.drawable.ic_baseline_search_24)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, newId: Int) {
                if (newId == R.id.end) {
                    binding.searchInput.requestFocus()
                    binding.searchInput.showKeyboard()
                } else {
                    binding.searchInput.clearFocus()
                    binding.searchInput.hideKeyboard()
                }
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })
    }

    override fun onPause() {
        super.onPause()
        binding.searchInput.hideKeyboard()
    }

    private fun observeCommands() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    is ScrollToPosition -> if (it.position == 0) {
                        binding.recyclerView.scrollTo(0, 0)
                    } else {
                        (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            it.position,
                            0
                        )
                    }
                    is RefreshFailed -> requireContext().showToast(R.string.request_failed)
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.refreshLayout.isRefreshing = it.loading
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
        setNavigationBarColor(R.color.white)
        useLightStatusBarIcons(false)
        drawBelowStatusBar(true)
        drawBelowNavigationBar(false)
    }
}