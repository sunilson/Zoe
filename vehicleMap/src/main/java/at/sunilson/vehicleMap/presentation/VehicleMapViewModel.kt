package at.sunilson.vehicleMap.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import at.sunilson.core.extensions.doOnFailure
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
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.coroutines.transformFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.strict.orbit
import org.orbitmvi.orbit.syntax.strict.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

internal data class VehicleMapState(
    val location: Location? = null,
    val loading: Boolean = false,
    val loadingChargingStations: Boolean = false,
    val reachableArea: ReachableArea? = null,
    val chargingStationsVisible: Boolean = false
)

internal class VehicleMapSideEffects

internal class VehicleMapViewModel @ViewModelInject constructor(
    private val refreshVehicleLocation: RefreshVehicleLocation,
    private val getSelectedVehicle: GetSelectedVehicle,
    private val getVehicleLocations: GetVehicleLocations,
    private val getReachableArea: GetReachableArea,
    private val getChargingStationsForSelectedVehicle: GetChargingStationsForSelectedVehicle
) : ViewModel(), ContainerHost<VehicleMapState, VehicleMapSideEffects> {

    override val container = container<VehicleMapState, VehicleMapSideEffects>(VehicleMapState())

    private val _chargingStations = MutableStateFlow<List<ChargingStation>>(listOf())
    val chargingStations: Flow<List<ChargingStation>>
        get() = _chargingStations

    private var lastRadius: Double? = null

    init {
        orbit {
            transformFlow { getSelectedVehicle(Unit) }.reduce {
                state.copy(location = event?.location)
            }
        }

        orbit {
            transformFlow { getReachableArea(Unit) }.reduce {
                state.copy(reachableArea = event)
            }
        }
    }

    fun loadLocationList(vin: String): Flow<List<Location>> = getVehicleLocations(vin)

    fun refreshPosition(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshVehicleLocation(vin).doOnFailure { Timber.e(it) }
        reduce { state.copy(loading = false) }
    }

    fun toggleChargingStations() = intent {
        val toggled = !state.chargingStationsVisible

        if (toggled && lastRadius != null) {
            mapPositionChanged(lastRadius!!, forced = true)
        } else if (!toggled) {
            _chargingStations.value = listOf()
            reduce { state.copy(chargingStationsVisible = toggled) }
        }
    }

    fun mapPositionChanged(radius: Double, forced: Boolean = false) = intent {
        lastRadius = radius

        if (!state.chargingStationsVisible && !forced) return@intent

        reduce { state.copy(loadingChargingStations = true) }
        getChargingStationsForSelectedVehicle(radius).fold(
            {
                _chargingStations.value = it
                reduce { state.copy(chargingStationsVisible = true) }
            },
            { Timber.e(it) }
        )
        reduce { state.copy(loadingChargingStations = false) }
    }
}
