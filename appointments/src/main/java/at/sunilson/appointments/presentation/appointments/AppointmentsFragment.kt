package at.sunilson.appointments.presentation.appointments

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.appointments.R
import at.sunilson.appointments.databinding.FragmentAppointmentsBinding
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.core.Do
import at.sunilson.core.extensions.isSameMonth
import at.sunilson.ktx.context.showToast
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.presentationcore.extensions.syncProgressDialogVisibility
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.time.ZoneOffset

@AndroidEntryPoint
internal class AppointmentsFragment private constructor() :
    Fragment(R.layout.fragment_appointments) {

    private val viewModel by viewModels<AppointmentsViewModel>()
    private val binding by viewBinding(FragmentAppointmentsBinding::bind)
    private val vin: String
        get() = checkNotNull(requireArguments().getString("vin"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshAppointments(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEvents()

        binding.refreshLayout.setOnRefreshListener { viewModel.refreshAppointments(vin) }
        binding.refreshLayout.isEnabled = true

        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.loadAppointments(vin)
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(binding.recyclerView)
    }

    private fun addToCalendar(appointment: Appointment) {
        startActivity(Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, appointment.label)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                appointment.date?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                    ?: return@apply
            )
        })
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    is RequestFailed -> requireContext().showToast(
                        R.string.request_failed,
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                syncProgressDialogVisibility(it.changing)
                binding.refreshLayout.isRefreshing = it.loading
                renderList(it.appointments, it.annualMileage)
            }
        }
    }

    private fun renderList(appointments: List<Appointment>, annualMileage: Int) {
        binding.recyclerView.withModels {

            var previousAppointment: Appointment? = null

            appointmentsMileageSlider {
                id("slider")
                annualMileage(annualMileage)
                sliderValueChanged { viewModel.updateVehicleMileage(it, vin) }
            }

            appointments.forEachIndexed { index, appointment ->
                appointmentListItem {
                    id(index)
                    appointment(appointment)
                    addToCalendar { addToCalendar(it) }
                    headline(
                        if (previousAppointment == null
                            || !previousAppointment!!.date.isSameMonth(appointment.date)
                        ) {
                            appointment.date.formatPattern("MMMM YYYY")
                        } else {
                            null
                        }
                    )
                }
                previousAppointment = appointment
            }
        }
    }

    companion object {
        fun newInstance(vin: String) = AppointmentsFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}