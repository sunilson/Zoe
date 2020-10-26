package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.core.extensions.doOnFailure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleMap.domain.GetReachableArea
import at.sunilson.vehicleMap.domain.GetVehicleLocations
import at.sunilson.vehicleMap.domain.RefreshVehicleLocation
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class VehicleMapState(
    val location: Location? = null,
    val previousLocations: List<Location> = listOf(),
    val loading: Boolean = false,
    val reachableArea: ReachableArea? = null
)

internal class VehicleMapEvents

internal class VehicleMapViewModel @ViewModelInject constructor(
    private val refreshVehicleLocation: RefreshVehicleLocation,
    private val getSelectedVehicle: GetSelectedVehicle,
    private val getVehicleLocations: GetVehicleLocations,
    private val getReachableArea: GetReachableArea
) : UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    private var locationsJob: Job? = null

    init {
        viewModelScope.launch {
            getSelectedVehicle(Unit).collect { vehicle ->
                setState { copy(location = vehicle?.location) }
            }
        }

        viewModelScope.launch {
            getReachableArea(Unit).collect { reachableArea ->
                setState { copy(reachableArea = reachableArea) }
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