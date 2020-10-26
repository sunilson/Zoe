package at.sunilson.appointments.presentation.appointments

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.ChangeVehicleAnnualMileage
import at.sunilson.appointments.domain.GetAllAppointments
import at.sunilson.appointments.domain.RefreshAppointments
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.appointments.domain.entities.ChangeVehicleMileageParams
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class AppointmentsState(
    val loading: Boolean = false,
    val changing: Boolean = false,
    val appointments: List<Appointment> = listOf(),
    val annualMileage: Int = 10000
)

internal sealed class AppointmentsEvents
internal object RequestFailed : AppointmentsEvents()

internal class AppointmentsViewModel @ViewModelInject constructor(
    private val refreshAppointmentsUseCase: RefreshAppointments,
    private val getAllAppointments: GetAllAppointments,
    private val changeVehicleAnnualMileage: ChangeVehicleAnnualMileage,
    private val getSelectedVehicle: GetSelectedVehicle
) : UniDirectionalViewModel<AppointmentsState, AppointmentsEvents>(AppointmentsState()) {

    private var appointmentsJob: Job? = null
    private var vehicleJob: Job? = null

    fun loadAppointments(vin: String) {
        appointmentsJob?.cancel()
        appointmentsJob = viewModelScope.launch {
            getAllAppointments(vin).collect {
                setState { copy(appointments = it) }
            }
        }

        vehicleJob = viewModelScope.launch {
            getSelectedVehicle(Unit).collect {
                if (it == null) return@collect
                setState { copy(annualMileage = it.annualMileage) }
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

    fun updateVehicleMileage(mileage: Int, vin: String) {
        getState { state ->
            viewModelScope.launch {
                val previousMileage = state.annualMileage
                setState { copy(changing = true, annualMileage = mileage) }
                changeVehicleAnnualMileage(ChangeVehicleMileageParams(vin, mileage)).fold(
                    { refreshAppointments(vin) },
                    {
                        Timber.e(it)
                        setState { copy(annualMileage = previousMileage) }
                    }
                )
                setState { copy(changing = false) }
            }
        }
    }
}