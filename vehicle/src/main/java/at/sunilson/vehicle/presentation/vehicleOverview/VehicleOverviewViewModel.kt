package at.sunilson.vehicle.presentation.vehicleOverview

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.GetNearestAppointment
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.unidirectionalviewmodel.savedstate.Persist
import at.sunilson.unidirectionalviewmodel.savedstate.PersistableState
import at.sunilson.unidirectionalviewmodel.savedstate.UniDirectionalSavedStateViewModelReflection
import at.sunilson.vehicle.domain.GetSelectedVehicleCurrentChargeProcedure
import at.sunilson.vehicle.domain.RefreshAllVehicles
import at.sunilson.vehicle.domain.StartCharging
import at.sunilson.vehicle.domain.StartClimateControl
import at.sunilson.vehicle.domain.entities.ChargeProcedure
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.GetSelectedVehicleLocation
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@PersistableState
data class VehicleOverviewState(
    val loading: Boolean = false,
    @Persist
    val selectedVehicle: Vehicle? = null,
    @Persist
    val vehicleLocation: Location? = null,
    val currentChargeProcedure: ChargeProcedure? = null,
    val nextAppointment: Appointment? = null
)

sealed class VehicleOverviewEvents
object ShowSplashScreen : VehicleOverviewEvents()
object NoVehiclesAvailable : VehicleOverviewEvents()
object RequestFailed: VehicleOverviewEvents()
data class ShowToast(val message: String) : VehicleOverviewEvents()
data class ShowVehicleDetails(val vin: String) : VehicleOverviewEvents()
data class ShowVehicleStatistics(val vin: String) : VehicleOverviewEvents()
data class ShowChargeStatistics(val vin: String) : VehicleOverviewEvents()
data class ShowVehicleLocation(val vin: String) : VehicleOverviewEvents()

internal class VehicleOverviewViewModel @ViewModelInject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val refreshAllVehicles: RefreshAllVehicles,
    private val startClimateControlUseCase: StartClimateControl,
    private val startChargingUseCase: StartCharging,
    private val getSelectedVehicleLocation: GetSelectedVehicleLocation,
    private val getSelectedVehicleCurrentChargeProcedure: GetSelectedVehicleCurrentChargeProcedure,
    private val getNearestAppointment: GetNearestAppointment,
    @Assisted savedStateHandle: SavedStateHandle
) : UniDirectionalSavedStateViewModelReflection<VehicleOverviewState, VehicleOverviewEvents>(
    VehicleOverviewState(), savedStateHandle
) {

    private var selectedVehicleJob: Job? = null
    private var locationJob: Job? = null
    private var chargeProcedureJob: Job? = null
    private var nearestAppointmentJob: Job? = null
    private var refreshingJob: Job? = null

    init {
        loadSelectedVehicle()
    }

    fun refreshVehicles(invisible: Boolean = false) {
        refreshingJob?.cancel()
        refreshingJob = viewModelScope.launch {
            if (!invisible) setState { copy(loading = true) }

            refreshAllVehicles(Unit).fold(
                {
                    if (it.isEmpty()) {
                        sendEvent(NoVehiclesAvailable)
                    }
                },
                {
                    Timber.e(it)
                    getState { state ->
                        if (state.selectedVehicle == null) {
                            sendEvent(NoVehiclesAvailable)
                        } else {
                            sendEvent(RequestFailed)
                        }
                    }
                }
            )

            setState { copy(loading = false) }
        }
    }

    fun startClimateControl(vin: String, targetTemperature: Int = 21, startTime: Long? = null) {
        viewModelScope.launch {
            startClimateControlUseCase(vin).fold(
                { sendEvent(ShowToast("Klimatisierungs Anfrage gesendet!")) },
                { Timber.e(it) }
            )
        }
    }

    fun startCharging(vin: String) {
        viewModelScope.launch {
            startChargingUseCase(vin).fold(
                { sendEvent(ShowToast("Lade Anfrage gesendet!")) },
                { Timber.e(it) }
            )
        }
    }


    fun showVehicleLocation() {
        getState { it.selectedVehicle?.let { vehicle -> sendEvent(ShowVehicleLocation(vehicle.vin)) } }
    }

    fun showChargeStatistics() {
        getState { it.selectedVehicle?.let { vehicle -> sendEvent(ShowChargeStatistics(vehicle.vin)) } }
    }

    fun showVehicleStatistics() {
        getState { it.selectedVehicle?.let { vehicle -> sendEvent(ShowVehicleStatistics(vehicle.vin)) } }
    }

    fun showVehicleDetails() {
        getState { it.selectedVehicle?.let { vehicle -> sendEvent(ShowVehicleDetails(vehicle.vin)) } }
    }

    private fun loadSelectedVehicle() {
        selectedVehicleJob?.cancel()
        selectedVehicleJob = viewModelScope.launch {
            getSelectedVehicle(Unit).collect {
                if (it == null) {
                    Timber.e("Selected Vehicle was null")
                    sendEvent(ShowSplashScreen)
                } else {
                    setState { copy(selectedVehicle = it) }
                }
            }
        }

        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            getSelectedVehicleLocation(Unit).collect {
                setState { copy(vehicleLocation = it) }
            }
        }

        chargeProcedureJob?.cancel()
        chargeProcedureJob = viewModelScope.launch {
            getSelectedVehicleCurrentChargeProcedure(Unit).collect {
                setState { copy(currentChargeProcedure = it) }
            }
        }

        nearestAppointmentJob?.cancel()
        nearestAppointmentJob = viewModelScope.launch {
            getNearestAppointment(Unit).collect {
                setState { copy(nextAppointment = it) }
            }
        }
    }
}