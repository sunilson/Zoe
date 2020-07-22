package at.sunilson.vehicle.presentation.vehicleMap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleMapBinding
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.CameraPosition
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VehicleMapFragment : Fragment(R.layout.fragment_vehicle_map) {

    private val viewModel by viewModels<VehicleMapViewModel>()
    private val args by navArgs<VehicleMapFragmentArgs>()
    private var leaving = false
    private val binding by viewBinding(FragmentVehicleMapBinding::bind)
    private val mapFragment: SupportMapFragment
        get() = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    private var map: GoogleMap? = null
    private var previousMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = MaterialContainerTransform().apply { duration = 500 }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshLayout.isEnabled = false
        setupMap()
        observeState()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(leaving) return@addCallback

            leaving = true
            map?.snapshot {
                binding.snapshot.setImageBitmap(it)
                findNavController().navigateUp()
            }
        }
    }

    private fun setupMap() {
        mapFragment.getMapAsync {
            startPostponedEnterTransition()
            map = it
            viewModel.refreshPosition(args.vin)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPosition(args.vin)
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.refreshLayout.isRefreshing = it.loading
                setMarkerToLocation(it.location ?: return@collect)
            }
        }
    }

    private fun makeSnapshot(): Bitmap {
        val view = requireView()
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

    private fun setMarkerToLocation(location: Location) {
        previousMarker?.remove()
        previousMarker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(location.lat, location.lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.zoe))
        )
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        location.lat,
                        location.lng
                    ), 10f
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previousMarker?.remove()
        previousMarker = null
    }
}