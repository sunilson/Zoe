package at.sunilson.vehicle.presentation.vehicleOverview

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.savedstate.Persist
import at.sunilson.unidirectionalviewmodel.savedstate.PersistableState
import at.sunilson.unidirectionalviewmodel.savedstate.UniDirectionalSavedStateViewModelReflection
import at.sunilson.vehicle.domain.GetSelectedVehicle
import at.sunilson.vehicle.domain.LocateSelectedVehicle
import at.sunilson.vehicle.domain.RefreshAllVehicles
import at.sunilson.vehicle.domain.StartClimateControl
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
    val vehicleLocation: Location? = null
)

sealed class VehicleOverviewEvents
data class ShowToast(val message: String) : VehicleOverviewEvents()
data class ShowVehicleDetails(val vin: String) : VehicleOverviewEvents()
data class ShowVehicleStatistics(val vin: String) : VehicleOverviewEvents()
data class ShowChargeStatistics(val vin: String) : VehicleOverviewEvents()

internal class VehicleOverviewViewModel @ViewModelInject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val refreshAllVehicles: RefreshAllVehicles,
    private val startClimateControl: StartClimateControl,
    private val locateSelectedVehicle: LocateSelectedVehicle,
    @Assisted savedStateHandle: SavedStateHandle
) : UniDirectionalSavedStateViewModelReflection<VehicleOverviewState, VehicleOverviewEvents>(
    VehicleOverviewState(), savedStateHandle
) {

    private var selectedVehicleJob: Job? = null
    private var locationJob: Job? = null
    private var refreshingJob: Job? = null

    init {
        loadSelectedVehicle()
        updateVehicleLocation()
    }

    fun refreshVehicles(invisible: Boolean = false) {
        refreshingJob?.cancel()
        refreshingJob = viewModelScope.launch {
            if (!invisible) setState { copy(loading = true) }

            refreshAllVehicles(Unit).fold(
                { updateSelectedVehicle() },
                {
                    sendEvent(ShowToast("Beim Aktualisieren ist ein Fehler aufgetreten!"))
                    Timber.e(it)
                }
            )

            setState { copy(loading = false) }
        }
    }

    fun startClimateControl(targetTemperature: Int = 21, startTime: Long? = null) {
        viewModelScope.launch {
            startClimateControl(Unit).fold(
                { sendEvent(ShowToast("Klimatisierungs Anfrage gesendet!")) },
                { Timber.e(it) }
            )
        }
    }

    fun showChargeStatistics() {
        getState {
            it.selectedVehicle?.let { vehicle ->
                sendEvent(ShowChargeStatistics(vehicle.vin))
            }
        }
    }

    fun showVehicleStatistics() {
        getState {
            it.selectedVehicle?.let { vehicle ->
                sendEvent(ShowVehicleStatistics(vehicle.vin))
            }
        }
    }

    fun showVehicleDetails() {
        getState {
            it.selectedVehicle?.let { vehicle ->
                sendEvent(ShowVehicleDetails(vehicle.vin))
            }
        }
    }

    private fun loadSelectedVehicle() {
        selectedVehicleJob?.cancel()
        selectedVehicleJob = viewModelScope.launch {
            getSelectedVehicle(Unit).collect {
                if (it == null) {
                    Timber.e("Selected Vehicle was null")
                    //TODO("Move to vehicle list")
                } else {
                    setState { copy(selectedVehicle = it) }
                }
            }
        }
    }

    private fun updateVehicleLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            locateSelectedVehicle(Unit).fold(
                { setState { copy(vehicleLocation = it) } },
                { Timber.e(it) }
            )
        }
    }

    private fun updateSelectedVehicle() {
        getState { state ->
            if (state.selectedVehicle == null) {
                loadSelectedVehicle()
            }
        }
    }
}