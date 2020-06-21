package at.sunilson.vehicle.presentation.vehicleOverview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.entities.Vehicle
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.unidirectionalviewmodel.savedstate.PersistableState
import at.sunilson.vehicle.domain.GetSelectedVehicle
import at.sunilson.vehicle.domain.RefreshAllVehicles
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@PersistableState
data class VehicleOverviewState(val loading: Boolean = false, val selectedVehicle: Vehicle? = null)

class VehicleOverviewEvents

class VehicleOverviewViewModel @ViewModelInject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val refreshAllVehicles: RefreshAllVehicles
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