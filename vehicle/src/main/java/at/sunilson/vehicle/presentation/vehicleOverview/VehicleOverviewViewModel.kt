package at.sunilson.vehicle.presentation.vehicleOverview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.entities.Vehicle
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.unidirectionalviewmodel.savedstate.PersistableState
import at.sunilson.vehicle.domain.GetSelectedVehicle
import at.sunilson.vehicle.domain.RefreshAllVehicles
import at.sunilson.vehicle.domain.StartClimateControl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@PersistableState
data class VehicleOverviewState(val loading: Boolean = false, val selectedVehicle: Vehicle? = null)

sealed class VehicleOverviewEvents
data class ShowToast(val message: String) : VehicleOverviewEvents()
data class ShowVehicleDetails(val vin: String): VehicleOverviewEvents()

class VehicleOverviewViewModel @ViewModelInject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val refreshAllVehicles: RefreshAllVehicles,
    private val startClimateControl: StartClimateControl
) : UniDirectionalViewModel<VehicleOverviewState, VehicleOverviewEvents>(VehicleOverviewState()) {

    private var selectedVehicleJob: Job? = null

    init {
        loadSelectedVehicle()
    }


    fun refreshVehicles() {
        viewModelScope.launch {
            refreshAllVehicles(Unit).fold(
                { updateSelectedVehicle() },
                { Timber.e(it) }
            )
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

    private fun updateSelectedVehicle() {
        getState { state ->
            if (state.selectedVehicle == null) {
                loadSelectedVehicle()
            }
        }
    }
}