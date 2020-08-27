package at.sunilson.vehicle.presentation.vehicleOverview

import android.animation.ValueAnimator
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.arkulpa.notifications.domain.NotificationRepository
import at.arkulpa.notifications.presentation.notificationWidget
import at.arkulpa.widget.VehicleWidgetProvider
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.ktx.navigation.navigateSafe
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.presentationcore.extensions.withDefaultAnimations
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleOverviewBinding
import at.sunilson.vehicle.domain.entities.ChargeProcedure
import at.sunilson.vehicle.presentation.extensions.displayName
import at.sunilson.vehicle.presentation.hvacBroadcastReciever.HvacBroadCastReciever
import at.sunilson.vehicle.presentation.utils.TimeUtils
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.chargeWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.climateControlWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.statisticsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleDetailsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleLocationWidget
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class VehicleOverviewFragment : Fragment(R.layout.fragment_vehicle_overview) {

    private val binding by viewBinding(FragmentVehicleOverviewBinding::bind)
    private val viewModel by viewModels<VehicleOverviewViewModel>()
    private val animators = mutableListOf<ValueAnimator>()
    private var lastBackPress: Long = -1L
    private var splashAnimationStarted = false


    @Inject
    lateinit var logoutHandler: LogoutHandler

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshVehicles()
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

    override fun onDestroyView() {
        animators.forEach { it.cancel() }
        animators.clear()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition = null

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { tryExit() }

        setupClickListeners()
        setupSwipeRefreshLayout()
        observeState()
        observeEvents()
        setupUIFlags()
        setupInsets()
        setupList()

        setupHeaderAnimation(binding.cutView, binding.recyclerView, true)
    }

    private fun setupList() {
        //Use extra space to prefetch more items for smoother scrolling
        binding.recyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun calculateExtraLayoutSpace(
                state: RecyclerView.State,
                extraLayoutSpace: IntArray
            ) {
                extraLayoutSpace[1] = 2000
            }
        }

        binding.recyclerView.post { startPostponedEnterTransition() }
    }

    private fun startSplashAnimation() {
        Timber.d("Starting splash animation")
        binding.splashContainer.isVisible = true

        useLightStatusBarIcons(false)
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
                doOnEnd { finishSplashAnimation() }
                animators.add(this)
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
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                if (viewModel.state.first().selectedVehicle != null) {
                    break
                }
                delay(300L)
            }

            val halfScreenWidth = requireView().width / 2f
            val halfVehicleWidth = binding.splashVehicle.width / 2f

            val delay = 1500L

            ValueAnimator.ofFloat(0f, halfScreenWidth + halfVehicleWidth).apply {
                startDelay = delay
                duration = 500L
                interpolator = AccelerateInterpolator()

                addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    binding.splashVehicle.translationX = -animatedValue
                }

                animators.add(this)
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

                animators.add(this)
                start()
                doOnEnd {
                    binding.splashContainer.visibility = View.GONE
                    splashAnimationStarted = false
                    useLightStatusBarIcons(true)
                }
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
        binding.vehicleImage.setOnClickListener { viewModel.showVehicleDetails() }
        binding.settingsButton.setOnClickListener {
            navigateSafe(R.id.show_settings_dialog)
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect { event ->
                Do exhaustive when (event) {
                    is ShowToast -> requireContext().showToast(event.message)
                    is ShowVehicleDetails -> showVehicleDetails(event.vin)
                    is ShowVehicleStatistics -> findNavController().navigate("https://zoe.app/statistics/${event.vin}".toUri())
                    is ShowChargeStatistics -> showChargeStatistics(event.vin)
                    is ShowVehicleLocation -> showVehicleLocation(event.vin)
                    is NoVehiclesAvailable -> logoutHandler.emitLogout()
                    is ShowSplashScreen -> if (!splashAnimationStarted) {
                        splashAnimationStarted = true
                        startSplashAnimation()
                    } else {
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                binding.contentContainer.isRefreshing = state.loading
                if (state.selectedVehicle != null) {
                    updateShortcuts(state.selectedVehicle)
                    renderVehicle(
                        state.selectedVehicle,
                        state.vehicleLocation,
                        state.currentChargeProcedure
                    )
                    updateVehicleWidget()
                }
            }
        }
    }

    private fun showVehicleLocation(vin: String) {
        exitTransition = Hold()
        val mapView = requireView().findViewById<MaterialCardView>(R.id.location_widget)
        if (mapView != null) {
            findNavController().navigate(
                Uri.parse("zoe://vehicle_location/$vin"),
                null,
                FragmentNavigatorExtras(mapView to "location")
            )
        } else {
            findNavController().navigate(Uri.parse("zoe://vehicle_location/$vin"))
        }
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

    private fun showChargeStatistics(vin: String, manage: Boolean = false) {
        reenterTransition = null
        exitTransition = null
        findNavController()
            .navigate(
                Uri.parse("zoe://charge_statistics/$vin?manage=$manage"),
                NavOptions.Builder().withDefaultAnimations()
            )
    }


    private fun renderVehicle(
        vehicle: Vehicle,
        location: Location?,
        chargeProcedure: ChargeProcedure?
    ) {
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
        binding.vehicleImageSmall.load(vehicle.imageUrl)
        binding.recyclerView.withModels {

            vehicleDetailsWidget {
                id("vehicleDetailsWidget")
                vehicle(vehicle)
                onButtonClick(this@VehicleOverviewFragment::showVehicleDetails)
            }

            chargeWidget {
                id("batteryStatusWidget")
                vehicle(vehicle)
                chargeScheduleClicked {
                    findNavController().navigate(
                        Uri.parse("zoe://charge_schedule/${vehicle.vin}"),
                        NavOptions.Builder().withDefaultAnimations()
                    )
                }
                chargeNowClicked { viewModel.startCharging(vehicle.vin) }
                currentChargeProcedure(chargeProcedure)
            }

            climateControlWidget {
                id("climateControlWidget")
                planClimateControlClicked { requireContext().showToast(R.string.not_available_yet) }
                startClimateControlClicked { viewModel.startClimateControl(vehicle.vin) }
            }

            if (location != null) {
                vehicleLocationWidget {
                    id("vehicleLocationWidget")
                    vehicle(vehicle)
                    location(location)
                    onMapClick { showVehicleLocation(it) }
                }
            }

            statisticsWidget {
                id("statisticsWidget")
                onHvacClick { requireContext().showToast(getString(R.string.not_available_yet)) }
                onChargeClick { viewModel.showChargeStatistics() }
            }

            notificationWidget {
                id("notificationsWidget")
                notificationRepository(notificationRepository)
                vin(vehicle.vin)
                chargeTrackButtonClicked { showChargeStatistics(it, manage = true) }
            }
        }
    }

    private fun setupUIFlags() {
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(!splashAnimationStarted)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }

    private fun updateVehicleWidget() {
        val context = requireContext()
        val intent = Intent(context, VehicleWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager
                .getInstance(context)
                .getAppWidgetIds(ComponentName(context, VehicleWidgetProvider::class.java))
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        context.sendBroadcast(intent)
    }

    private fun updateShortcuts(vehicle: Vehicle) {
        val shortcutManager =
            getSystemService<ShortcutManager>(requireContext(), ShortcutManager::class.java)

        val hvacShortcut = ShortcutInfo.Builder(context, "startClimateControl")
            .setShortLabel(getString(R.string.start_hvac_shortcut))
            .setLongLabel(getString(R.string.start_hvac_shortcut_long))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_baseline_ac_unit_24))
            .setIntent(
                Intent().apply {
                    action = "StartHVAC"
                    component = ComponentName(requireContext(), HvacBroadCastReciever::class.java)
                }
            )
            .build()

        val locationShortcut = ShortcutInfo.Builder(context, "location")
            .setShortLabel(getString(R.string.map_shortcut))
            .setLongLabel(getString(R.string.map_shortcut_long))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_baseline_map_24))
            .setIntent(
                Intent().apply {
                    data = Uri.parse("zoe://vehicle_location/${vehicle.vin}")
                    action = Intent.ACTION_VIEW
                }
            )
            .build()

        val chargeStatisticsShortcut = ShortcutInfo.Builder(context, "chargeStatistics")
            .setShortLabel(getString(R.string.show_charge_statistics_shortcut))
            .setLongLabel(getString(R.string.show_charge_statistics_shortcut_long))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_baseline_ev_station_24))
            .setIntent(
                Intent().apply {
                    data = Uri.parse("zoe://charge_statistics/${vehicle.vin}")
                    action = Intent.ACTION_VIEW
                }
            )
            .build()

        shortcutManager?.dynamicShortcuts =
            listOf(hvacShortcut, locationShortcut, chargeStatisticsShortcut)
    }

    override fun onResume() {
        super.onResume()
        setupUIFlags()
        // TODO Only not on first app start viewModel.refreshVehicles(true)
    }
}