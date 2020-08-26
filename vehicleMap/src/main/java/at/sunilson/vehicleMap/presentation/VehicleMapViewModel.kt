package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.core.extensions.doOnFailure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleMap.domain.GetVehicleLocations
import at.sunilson.vehiclecore.domain.GetSelectedVehicleLocation
import at.sunilson.vehiclecore.domain.RefreshVehicleLocation
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
    private val refreshVehicleLocation: RefreshVehicleLocation,
    private val getVehicleLocation: GetSelectedVehicleLocation,
    private val getVehicleLocations: GetVehicleLocations
) : UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    private var locationsJob: Job? = null

    init {
        viewModelScope.launch {
            getVehicleLocation(Unit).collect {
                setState { copy(location = it) }
            }
        }
    }

    fun loadLocationList(vin: String) {
        locationsJob?.cancel()
        locationsJob = viewModelScope.launch {
            getVehicleLocations(vin).collect {
                setState { copy(previousLocations = it) }
            }
        }
    }

    fun refreshPosition(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshVehicleLocation(vin).doOnFailure { Timber.e(it) }
            setState { copy(loading = false) }
        }
    }

}