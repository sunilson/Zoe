package at.sunilson.vehicle.presentation.vehicleOverview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import at.sunilson.ktx.fragment.drawBelowNavigationBar
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
import at.sunilson.vehicle.presentation.hvacBroadcastReciever.HvacBroadCastReciever
import at.sunilson.vehicle.presentation.settingsDialog.SettingsDialogFragment.Companion.SETTINGS_DIALOG_REQUEST
import at.sunilson.vehicle.presentation.settingsDialog.SettingsDialogFragment.Companion.THEME_CHANGED_RESULT
import at.sunilson.vehicle.presentation.utils.TimeUtils
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.chargeWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.climateControlWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.servicesWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.statisticsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleDetailsWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.vehicleLocationWidget
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehiclecore.presentation.extensions.displayName
import coil.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.hypot


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

        //Use viewmodel so it is already initialized
        viewModel
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

        setupThemeReveal()
        setupClickListeners()
        setupSwipeRefreshLayout()
        observeState()
        observeEvents()
        setupUIFlags()
        setupInsets()
        setupList()
        setupResultListeners()

        setupHeaderAnimation(binding.cutView, binding.recyclerView)
    }

    private fun setupList() {
        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

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
        binding.settingsButton.applySystemWindowInsetsToMargin(bottom = true)
        binding.recyclerView.applySystemWindowInsetsToPadding(bottom = true)
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
                    is RequestFailed -> requireContext().showToast(
                        R.string.request_failed,
                        Toast.LENGTH_LONG
                    )
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
                    renderVehicle(state)
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
        findNavController()
            .navigate(
                Uri.parse("zoe://vehicle/$vin?smallTransition=${binding.motionLayout.progress != 0f}"),
                NavOptions.Builder().withDefaultAnimations(),
                FragmentNavigatorExtras(
                    if (binding.motionLayout.progress == 0f) {
                        binding.vehicleImage to "vehicleImage"
                    } else {
                        binding.vehicleImageSmall to "vehicleImageSmall"
                    }
                )
            )
    }

    private fun revealNewTheme() {
        binding.themeOverlay.doOnLayout {
            val cx = binding.themeOverlay.width / 2
            val cy = binding.themeOverlay.height / 2
            val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim =
                ViewAnimationUtils.createCircularReveal(binding.themeOverlay, cx, cy, radius, 0f)
            anim.duration = 1000
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.themeOverlay.visibility = View.GONE
                }
            })
            anim.start()
        }
    }

    private suspend fun takeScreenshot() = suspendCancellableCoroutine<Bitmap?> {
        val view = requireView()
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)
        PixelCopy.request(
            requireActivity().window,
            Rect(
                locationOfViewInWindow[0],
                locationOfViewInWindow[1],
                locationOfViewInWindow[0] + view.width,
                locationOfViewInWindow[1] + view.height
            ),
            bitmap,
            { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    it.resume(bitmap)
                } else {
                    it.resume(null)
                }
            },
            Handler()
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


    private fun renderVehicle(state: VehicleOverviewState) {
        val vehicle = state.selectedVehicle ?: return

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
                onButtonClick { showVehicleDetails(it.vin) }
            }

            climateControlWidget {
                id("climateControlWidget")
                planClimateControlClicked {
                    findNavController().navigate(
                        Uri.parse("zoe://hvac_schedule/${vehicle.vin}"),
                        NavOptions.Builder().withDefaultAnimations()
                    )
                }
                startClimateControlClicked { viewModel.startClimateControl(vehicle.vin) }
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
                currentChargeProcedure(state.currentChargeProcedure)
            }

            if (vehicle.location != null) {
                vehicleLocationWidget {
                    id("vehicleLocationWidget")
                    vehicle(vehicle)
                    location(vehicle.location)
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

            servicesWidget {
                id("servicesWidget")
                contract(state.nextContract)
                appointment(state.nextAppointment)
                allContractsClicked {
                    findNavController().navigate(
                        Uri.parse("zoe://contracts/${vehicle.vin}"),
                        NavOptions.Builder().withDefaultAnimations()
                    )
                }
                allAppointmentsClicked {
                    findNavController().navigate(
                        Uri.parse("zoe://appointments/${vehicle.vin}"),
                        NavOptions.Builder().withDefaultAnimations()
                    )
                }
            }
        }
    }

    private fun setupUIFlags() {
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.transparent)
        useLightStatusBarIcons(!splashAnimationStarted)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
        drawBelowNavigationBar()
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

    private fun setupThemeReveal() {
        if (viewModel.themeImage != null) {
            binding.themeOverlay.setImageBitmap(viewModel.themeImage)
            revealNewTheme()
            viewModel.themeImage = null
        }
    }

    private fun setupResultListeners() {
        setFragmentResultListener(SETTINGS_DIALOG_REQUEST) { _, bundle ->
            if (bundle.getBoolean(THEME_CHANGED_RESULT, false)) {
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    delay(500)
                    viewModel.themeImage = takeScreenshot()
                    requireActivity().recreate()
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupUIFlags()
        // TODO Only not on first app start viewModel.refreshVehicles(true)
    }
}