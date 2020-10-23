package at.sunilson.vehicleMap.presentation

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
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
import at.sunilson.presentationcore.extensions.getThemeColor
import at.sunilson.vehicleMap.R
import at.sunilson.vehicleMap.databinding.FragmentVehicleMapBinding
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
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
    private var previousMarker: Marker? = null
    private var previousLine: Polyline? = null
    private var previousArea: Polygon? = null
    private var previousReachableArea: ReachableArea? = null
    private var previousLocations: List<Location>? = null

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
        mapFragment?.getMapAsync {
            startPostponedEnterTransition()
            map = it
            viewModel.refreshPosition(args.vin)
            viewModel.loadLocationList(args.vin)
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
            viewModel.state.collect {
                updateFab(it.loading)
                drawLocationsLine(it.previousLocations)
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
        if(reachableArea == previousReachableArea) return
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

        centerReachableArea()
    }

    private fun centerReachableArea() {
        previousReachableArea?.let { reachableArea ->
            map?.animateCamera(CameraUpdateFactory.newLatLngBounds(reachableArea.boundingBox, 50))
        }
    }

    private fun centerCar() {
        previousMarker?.let {marker ->
            map?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(marker.position, 14f)
                )
            )
        }
    }

    private fun drawLocationsLine(locations: List<Location>) {
        if(locations == previousLocations) return
        previousLocations = locations

        previousLine?.remove()
        previousLine = map?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .zIndex(1f)
                .width(5f)
                .jointType(JointType.ROUND)
                .color(requireContext().getThemeColor(R.attr.colorPrimary))
                .addAll(locations.map { LatLng(it.lat, it.lng) })
        )
    }

    private fun updateFab(loading: Boolean) {
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

    private fun setMarkerToLocation(location: Location) {
        previousMarker?.remove()
        previousMarker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(location.lat, location.lng))
                .zIndex(2f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.zoe))
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previousMarker?.remove()
        previousMarker = null
    }
}