package at.sunilson.vehicleMap.presentation

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.ktx.fragment.drawBelowNavigationBar
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatFull
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.presentationcore.extensions.getThemeColor
import at.sunilson.vehicleMap.R
import at.sunilson.vehicleMap.databinding.ChargingStationInfoWindowContentBinding
import at.sunilson.vehicleMap.databinding.FragmentVehicleMapBinding
import at.sunilson.vehicleMap.domain.entities.ChargingStation
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehicleMap.domain.position
import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.CameraPosition
import com.google.android.libraries.maps.model.JointType
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.libraries.maps.model.Polygon
import com.google.android.libraries.maps.model.PolygonOptions
import com.google.android.libraries.maps.model.Polyline
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.android.libraries.maps.model.StrokeStyle
import com.google.android.libraries.maps.model.StyleSpan
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VehicleMapFragment : Fragment(R.layout.fragment_vehicle_map) {

    private val viewModel by viewModels<VehicleMapViewModel>()
    private val args by navArgs<VehicleMapFragmentArgs>()
    private var leaving = false
    private val binding by viewBinding(FragmentVehicleMapBinding::bind)
    private val mapFragment: SupportMapFragment?
        get() = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

    private var map: GoogleMap? = null
    private val charingStationMarkers = mutableListOf<Marker>()
    private var previousMarker: Marker? = null
    private var previousLine: Polyline? = null
    private var previousArea: Polygon? = null
    private var previousReachableArea: ReachableArea? = null
    private var cachedChargingStations: List<ChargingStation> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = MaterialContainerTransform().apply { duration = 500 }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        observeState()

        binding.backButton.setOnClickListener { back() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { back() }

        binding.refreshFab.setOnClickListener { viewModel.refreshPosition(args.vin) }

        binding.refreshFab.applySystemWindowInsetsToMargin(bottom = true)
        binding.backButton.applySystemWindowInsetsToMargin(top = true)
        binding.chargingStationButton.setOnClickListener { viewModel.toggleChargingStations() }
        binding.centerAreaButton.setOnClickListener { centerReachableArea() }
        binding.centerCarButton.setOnClickListener { centerCar() }
    }

    private fun back() {
        if (leaving) return

        leaving = true
        map?.snapshot {
            binding.snapshot.setImageBitmap(it)
            findNavController().navigateUp()
        }
    }

    private fun setupMap() {
        mapFragment?.getMapAsync { map ->
            map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoWindow(p0: Marker): View? = null
                override fun getInfoContents(marker: Marker): View? {
                    val chargingStation =
                        cachedChargingStations
                            .firstOrNull { it.location == marker.position }
                            ?: return null
                    val binding =
                        ChargingStationInfoWindowContentBinding.inflate(layoutInflater, null, false)
                    binding.name.text = chargingStation.name
                    binding.freeSpaces.text = "Freie Plätze: ${chargingStation.availableSpots}"
                    binding.address.text = chargingStation.address
                    binding.openingTimes.text = chargingStation
                        .openingTimes
                        .joinToString("") { "${it.dayOfWeek.formatPattern("EEEE")}: ${it.startTime} - ${it.endTime}\n" }
                        .trim()
                    binding.paymentModes.text = chargingStation
                            .paymentModes
                            .joinToString("") { "\u2022 $it\n" }
                            .trim()
                    return binding.root
                }
            })
            startPostponedEnterTransition()
            this.map = map
            viewModel.refreshPosition(args.vin)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPosition(args.vin)
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.transparent)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
        drawBelowNavigationBar()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.chargingStations.collect {
                drawChargingLocations(it)
                cachedChargingStations = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loadLocationList(args.vin).collect {
                drawLocationsLine(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.chargingStationButton.isEnabled = !it.loadingChargingStations
                updateVehicleFab(it.loading)
                if (it.reachableArea != null) {
                    drawReachableArea(it.reachableArea)
                }
                binding.refreshFab.text = it.location?.timestamp?.toZonedDateTime()?.formatFull()
                    ?: requireContext().getString(R.string.nothing)
                setMarkerToLocation(it.location ?: return@collect)
            }
        }
    }

    private fun drawReachableArea(reachableArea: ReachableArea) {
        if (reachableArea == previousReachableArea) return
        previousReachableArea = reachableArea

        previousArea?.remove()
        previousArea = map?.addPolygon(
            PolygonOptions()
                .clickable(false)
                .zIndex(1f)
                .strokeWidth(5f)
                .fillColor(requireContext().getThemeColor(R.attr.colorPrimaryLight))
                .strokeColor(requireContext().getThemeColor(R.attr.colorPrimary))
                .addAll(reachableArea.coordinates)
        )
    }

    private fun centerReachableArea() {
        previousReachableArea?.let { reachableArea ->
            map?.animateCamera(CameraUpdateFactory.newLatLngBounds(reachableArea.boundingBox, 50))
        }
    }

    private fun centerCar() {
        previousMarker?.let { marker ->
            map?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(marker.position, 14f)
                )
            )
        }
    }

    private fun drawChargingLocations(locations: List<ChargingStation>) {
        val map = map ?: return
        locations
            .filter { station -> charingStationMarkers.none { it.position == station.location } }
            .forEach { station ->
                station.location?.let {
                    charingStationMarkers.add(
                        map.addMarker(
                            MarkerOptions()
                                .position(it)
                                .zIndex(1f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.charging_station_pin))
                                .anchor(0.5f, 1f)
                        )
                    )
                }
            }
    }

    private fun drawLocationsLine(locations: List<Location>) {
        if (locations.isEmpty()) return
        val primaryColor = requireContext().getThemeColor(R.attr.colorPrimary)

        previousLine?.remove()
        previousLine = map?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .zIndex(1f)
                .width(5f)
                .jointType(JointType.ROUND)
                .color(requireContext().getThemeColor(R.attr.colorPrimary))
                .addAll(locations.map { LatLng(it.lat, it.lng) })
                .addSpan(
                    StyleSpan(
                        StrokeStyle.gradientBuilder(
                            ColorUtils.setAlphaComponent(primaryColor, 0),
                            primaryColor
                        ).build()
                    )
                )
        )
    }

    private fun updateVehicleFab(loading: Boolean) {
        if (loading) {
            binding.refreshFab.shrink()
            binding.refreshFab.setIconResource(R.drawable.animated_refresher)
            (binding.refreshFab.icon as AnimatedVectorDrawable).run {
                registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        this@run.start()
                    }
                })
                start()
            }
        } else {
            binding.refreshFab.extend()
            binding.refreshFab.setIconResource(R.drawable.ic_baseline_refresh_24)
        }

        binding.refreshFab.isEnabled = !loading
    }

    private fun setMarkerToLocation(location: Location) = map?.let { map ->
        if (previousMarker?.position == location.position) return@let

        previousMarker?.remove()
        previousMarker = map.addMarker(
            MarkerOptions()
                .position(LatLng(location.lat, location.lng))
                .zIndex(2f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.zoe))
        )

        centerCar()

        map.setOnCameraIdleListener {
            val visibleRegion = map.projection.visibleRegion.latLngBounds
            val center = previousMarker?.position ?: return@setOnCameraIdleListener
            val radius = FloatArray(1)
            android.location.Location.distanceBetween(
                center.latitude,
                center.longitude,
                visibleRegion.northeast.latitude,
                visibleRegion.northeast.longitude,
                radius
            )
            viewModel.mapPositionChanged(radius.first() / 1000.0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previousMarker?.remove()
        previousMarker = null
    }
}