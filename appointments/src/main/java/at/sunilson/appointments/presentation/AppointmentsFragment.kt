package at.sunilson.appointments.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.sunilson.appointments.R
import at.sunilson.appointments.databinding.FragmentAppointmentsBinding
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class AppointmentsFragment : Fragment(R.layout.fragment_appointments) {

    private val viewModel by viewModels<AppointmentsViewModel>()
    private val binding by viewBinding(FragmentAppointmentsBinding::bind)
    private val args by navArgs<AppointmentsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshAppointments(args.vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEvents()

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.topContainer)

        setupHeaderAnimation(binding.topContainer, binding.recyclerView, true)
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.refreshLayout.setOnRefreshListener { viewModel.refreshAppointments(args.vin) }
        binding.refreshLayout.isEnabled = true

        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.loadAppointments(args.vin)
        }
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    is RequestFailed -> requireContext().showToast(R.string.request_failed, Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.refreshLayout.isRefreshing = it.loading
                renderList(it.appointments)
            }
        }
    }

    private fun renderList(appointments: List<Appointment>) {
        binding.recyclerView.withModels {
            appointments.forEachIndexed { index, appointment ->
                appointmentListItem {
                    id(index)
                    appointment(appointment)
                }
            }
        }
    }
}