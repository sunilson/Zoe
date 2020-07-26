package at.sunilson.vehicle.presentation.vehicleMap

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicle.domain.LocateSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import kotlinx.coroutines.launch
import timber.log.Timber

data class VehicleMapState(val location: Location? = null, val loading: Boolean = false)
class VehicleMapEvents

internal class VehicleMapViewModel @ViewModelInject constructor(private val locateSelectedVehicle: LocateSelectedVehicle) :
    UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

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