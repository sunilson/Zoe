package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleMap.domain.GetVehicleLocations
import at.sunilson.vehiclecore.domain.LocateSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

data class VehicleMapState(
    val location: Location? = null,
    val previousLocations: List<Location> = listOf(),
    val loading: Boolean = false
)

class VehicleMapEvents

internal class VehicleMapViewModel @ViewModelInject constructor(
    private val locateSelectedVehicle: LocateSelectedVehicle,
    private val getVehicleLocations: GetVehicleLocations
) : UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    private var locationsJob : Job? = null

    fun loadLocationList(vin: String) {
        locationsJob?.cancel()
        locationsJob = viewModelScope.launch {
            getVehicleLocations(vin).collect {
                setState { copy(previousLocations = it) }
            }
        }
    }

    fun refreshPosition() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            locateSelectedVehicle(Unit).fold(
                { setState { copy(location = it) } },
                { Timber.e(it) }
            )
            setState { copy(loading = false) }
        }
    }

}