package at.sunilson.appointments.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.GetAllAppointments
import at.sunilson.appointments.domain.RefreshAppointments
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class AppointmentsState(
    val loading: Boolean = false,
    val appointments: List<Appointment> = listOf()
)

internal sealed class AppointmentsEvents
internal object RequestFailed : AppointmentsEvents()

internal class AppointmentsViewModel @ViewModelInject constructor(
    private val refreshAppointmentsUseCase: RefreshAppointments,
    private val getAllAppointments: GetAllAppointments
) : UniDirectionalViewModel<AppointmentsState, AppointmentsEvents>(AppointmentsState()) {

    private var appointmentsJob: Job? = null

    fun loadAppointments(vin: String) {
        appointmentsJob?.cancel()
        appointmentsJob = viewModelScope.launch {
            getAllAppointments(vin).collect {
                setState { copy(appointments = it) }
            }
        }
    }

    fun refreshAppointments(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshAppointmentsUseCase(vin).fold(
                {},
                {
                    sendEvent(RequestFailed)
                    Timber.e(it)
                }
            )
            setState { copy(loading = false) }
        }
    }
}