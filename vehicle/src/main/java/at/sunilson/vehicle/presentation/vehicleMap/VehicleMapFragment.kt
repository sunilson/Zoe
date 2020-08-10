package at.sunilson.vehicle.presentation.vehicleMap

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
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatFull
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.FragmentVehicleMapBinding
import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
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

        binding.refreshFab.setOnClickListener { viewModel.refreshPosition() }

        Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.backButton)
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
            viewModel.refreshPosition()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPosition()
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                updateFab(it.loading)
                binding.refreshFab.text = it.location?.timestamp?.toZonedDateTime()?.formatFull()
                    ?: requireContext().getString(R.string.nothing)
                setMarkerToLocation(it.location ?: return@collect)
            }
        }
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