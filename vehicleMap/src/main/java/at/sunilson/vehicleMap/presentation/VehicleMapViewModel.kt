package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.core.extensions.doOnFailure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleMap.domain.GetReachableArea
import at.sunilson.vehicleMap.domain.GetVehicleLocations
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehiclecore.domain.GetSelectedVehicleLocation
import at.sunilson.vehiclecore.domain.RefreshVehicleLocation
import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.gms.maps.model.LatLng
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
    private val getVehicleLocation: GetSelectedVehicleLocation,
    private val getVehicleLocations: GetVehicleLocations,
    private val getReachableArea: GetReachableArea
) : UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    private var locationsJob: Job? = null

    init {
        viewModelScope.launch {
            getVehicleLocation(Unit).collect { location ->
                setState { copy(location = location) }
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