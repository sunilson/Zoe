package at.sunilson.appointments.presentation.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.ChangeVehicleAnnualMileage
import at.sunilson.appointments.domain.GetAllAppointments
import at.sunilson.appointments.domain.RefreshAppointments
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.appointments.domain.entities.ChangeVehicleMileageParams
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

internal data class AppointmentsState(
    val loading: Boolean = false,
    val changing: Boolean = false,
    val appointments: List<Appointment> = listOf(),
    val annualMileage: Int = 10000
)

internal sealed class AppointmentsSideEffects {
    object RequestFailed : AppointmentsSideEffects()
}

@HiltViewModel
internal class AppointmentsViewModel @Inject constructor(
    private val refreshAppointmentsUseCase: RefreshAppointments,
    private val getAllAppointments: GetAllAppointments,
    private val changeVehicleAnnualMileage: ChangeVehicleAnnualMileage,
    private val getSelectedVehicle: GetSelectedVehicle
) : ViewModel(), ContainerHost<AppointmentsState, AppointmentsSideEffects> {

    override val container =
        container<AppointmentsState, AppointmentsSideEffects>(AppointmentsState())

    private var appointmentsJob: Job? = null
    private var vehicleJob: Job? = null

    fun loadAppointments(vin: String) {
        appointmentsJob?.cancel()
        appointmentsJob = viewModelScope.launch {
            getAllAppointments(vin).collect {
                intent { reduce { state.copy(appointments = it) } }
            }
        }

        vehicleJob = viewModelScope.launch {
            getSelectedVehicle(Unit).collect {
                if (it == null) return@collect
                intent { reduce { state.copy(annualMileage = it.annualMileage) } }
            }
        }
    }

    fun refreshAppointments(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshAppointmentsUseCase(vin).fold(
            {},
            {
                postSideEffect(AppointmentsSideEffects.RequestFailed)
                Timber.e(it)
            }
        )
        reduce { state.copy(loading = false) }
    }

    fun updateVehicleMileage(mileage: Int, vin: String) = intent {
        val previousMileage = state.annualMileage
        reduce { state.copy(changing = true, annualMileage = mileage) }
        changeVehicleAnnualMileage(ChangeVehicleMileageParams(vin, mileage)).fold(
            { refreshAppointments(vin) },
            {
                Timber.e(it)
                reduce { state.copy(annualMileage = previousMileage) }
            }
        )
        reduce { state.copy(changing = false) }
    }
}
