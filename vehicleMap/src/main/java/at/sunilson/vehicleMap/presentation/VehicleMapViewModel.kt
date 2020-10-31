package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.core.extensions.doOnFailure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleMap.domain.GetChargingStationsForSelectedVehicle
import at.sunilson.vehicleMap.domain.GetReachableArea
import at.sunilson.vehicleMap.domain.GetVehicleLocations
import at.sunilson.vehicleMap.domain.RefreshVehicleLocation
import at.sunilson.vehicleMap.domain.entities.ChargingStation
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class VehicleMapState(
    val location: Location? = null,
    val loading: Boolean = false,
    val loadingChargingStations: Boolean = false,
    val reachableArea: ReachableArea? = null,
    val chargingStationsVisible: Boolean = false
)

internal class VehicleMapEvents

internal class VehicleMapViewModel @ViewModelInject constructor(
    private val refreshVehicleLocation: RefreshVehicleLocation,
    private val getSelectedVehicle: GetSelectedVehicle,
    private val getVehicleLocations: GetVehicleLocations,
    private val getReachableArea: GetReachableArea,
    private val getChargingStationsForSelectedVehicle: GetChargingStationsForSelectedVehicle
) : UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    private val _chargingStations = MutableStateFlow<List<ChargingStation>>(listOf())
    val chargingStations: Flow<List<ChargingStation>>
        get() = _chargingStations

    private var lastRadius: Double? = null

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

    fun loadLocationList(vin: String): Flow<List<Location>> = getVehicleLocations(vin)

    fun refreshPosition(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshVehicleLocation(vin).doOnFailure { Timber.e(it) }
            setState { copy(loading = false) }
        }
    }

    fun toggleChargingStations() {
        getState { state ->
            val toggled = !state.chargingStationsVisible

            if (toggled && lastRadius != null) {
                mapPositionChanged(lastRadius!!, forced = true)
            } else if (!toggled) {
                _chargingStations.value = listOf()
                setState { copy(chargingStationsVisible = toggled) }
            }
        }
    }

    fun mapPositionChanged(radius: Double, forced: Boolean = false) {
        lastRadius = radius

        getState { state ->
            if (!state.chargingStationsVisible && !forced) return@getState

            viewModelScope.launch {
                setState { copy(loadingChargingStations = true) }
                getChargingStationsForSelectedVehicle(radius).fold(
                    {
                        _chargingStations.value = it
                        setState { copy(chargingStationsVisible = true) }
                    },
                    { Timber.e(it) }
                )
                setState { copy(loadingChargingStations = false) }
            }
        }
    }
}