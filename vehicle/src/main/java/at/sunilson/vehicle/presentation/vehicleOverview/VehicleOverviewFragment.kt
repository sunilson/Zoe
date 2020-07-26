package at.sunilson.vehicle.presentation.vehicleOverview

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
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
import at.sunilson.vehicle.presentation.extensions.displayName
import at.sunilson.vehicle.presentation.utils.TimeUtils
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.batteryStatusWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.climateControlWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.statisticsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleDetailsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleLocationWidget
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Long.min

@AndroidEntryPoint
class VehicleOverviewFragment : Fragment(R.layout.fragment_vehicle_overview) {
    private val binding by viewBinding(FragmentVehicleOverviewBinding::bind)
    private val viewModel by viewModels<VehicleOverviewViewModel>()

    private var splashShownTimestamp: Long = SPLASH_NOT_SHOWN_YET
    private var splashShown: Boolean = false
    private var lastBackPress: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshVehicles()
        requireActivity().onBackPressedDispatcher.addCallback { tryExit() }
    }

    private fun tryExit() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPress < 2000) {
            requireActivity().finish()
        } else {
            requireContext().showToast("Erneut drücken um zu beenden", Toast.LENGTH_SHORT)
            lastBackPress = currentTime
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupSwipeRefreshLayout()
        observeState()
        observeEvents()
        setupUIFlags()
        setupInsets()
        setupHeaderAnimation(binding.cutView, binding.recyclerView, true)
        binding.recyclerView.post { startPostponedEnterTransition() }

        if (!splashShown) {
            startSplashAnimation()
        } else {
            binding.splashContainer.isVisible = false
        }
    }

    private fun startSplashAnimation() {
        requireView().doOnLayout {
            val halfScreenWidth = it.width / 2f
            val halfVehicleWidth = binding.splashVehicle.width / 2f

            ValueAnimator.ofFloat(halfScreenWidth + halfVehicleWidth, 0f).apply {
                duration = 1000L
                interpolator = AccelerateDecelerateInterpolator()

                addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    binding.splashVehicle.translationX = animatedValue
                }

                doOnStart { binding.splashVehicle.visibility = View.VISIBLE }
                doOnEnd { splashShownTimestamp = System.currentTimeMillis() }
                start()
            }

            val bounceAnimation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.vehicle_bounce).apply {
                    repeatCount = Animation.INFINITE
                }
            binding.splashVehicle.startAnimation(bounceAnimation)
        }
    }

    private fun finishSplashAnimation() {
        if (splashShown) return
        splashShown = true

        val halfScreenWidth = requireView().width / 2f
        val halfVehicleWidth = binding.splashVehicle.width / 2f

        val delay = when (splashShownTimestamp) {
            SPLASH_NOT_SHOWN_YET -> 500L
            else -> min(500L, System.currentTimeMillis() - splashShownTimestamp)
        }

        ValueAnimator.ofFloat(0f, halfScreenWidth + halfVehicleWidth).apply {
            startDelay = delay
            duration = 500L
            interpolator = AccelerateInterpolator()

            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                binding.splashVehicle.translationX = -animatedValue
            }

            start()
            doOnEnd { binding.splashVehicle.visibility = View.GONE }
        }

        ValueAnimator.ofFloat(1f, 0f).apply {
            startDelay = 500L + delay
            duration = 300L
            interpolator = AccelerateInterpolator()

            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                binding.splashContainer.alpha = animatedValue
            }

            start()
            doOnEnd {
                binding.splashContainer.visibility = View.GONE
                useLightStatusBarIcons(true)
            }
        }
    }

    private fun setupInsets() {
        binding.splashContainer.applySystemWindowInsetsToPadding(top = true)
        binding.motionLayout.applySystemWindowInsetsToPadding(top = true)
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
                    is ShowChargeStatistics -> showChargeStatistics(event.vin)
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.contentContainer.isRefreshing = state.loading
                if (state.selectedVehicle != null) {
                    if (!state.loading) {
                        finishSplashAnimation()
                    }
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
        reenterTransition = null
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

    private fun showChargeStatistics(vin: String) {
        reenterTransition = null
        exitTransition = null
        findNavController().navigate(VehicleOverviewFragmentDirections.showChargeStatistics(vin))
    }


    private fun renderVehicle(vehicle: Vehicle, location: Location?) {
        binding.vehicleSubtitle.text =
            if (vehicle.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGING) {
                "Laden: ${TimeUtils.formatMinuteDuration(vehicle.batteryStatus.remainingChargeTime)} verbleibend"
            } else {
                getString(vehicle.batteryStatus.chargeState.displayName)
            }
        binding.vehicleTitle.text =
            "${vehicle.batteryStatus.batteryLevel}% (${vehicle.batteryStatus.remainingRange} Km)"
        binding.progressBar.progress = vehicle.batteryStatus.batteryLevel.toFloat()
        binding.vehicleImage.load(vehicle.imageUrl)
        binding.splashVehicle.load(vehicle.imageUrl)
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

            statisticsWidget {
                id("statisticsWidget")
                onHvacClick { viewModel.showVehicleStatistics() }
                onChargeClick { viewModel.showChargeStatistics() }
            }
        }
    }

    private fun setupUIFlags() {
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(splashShown)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }

    override fun onResume() {
        super.onResume()
        setupUIFlags()
        if (splashShown) viewModel.refreshVehicles(true)
    }

    companion object {
        const val SPLASH_NOT_SHOWN_YET = -1L
    }
}