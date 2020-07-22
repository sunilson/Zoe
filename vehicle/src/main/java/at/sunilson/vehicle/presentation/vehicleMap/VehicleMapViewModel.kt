package at.sunilson.vehicle.presentation.vehicleMap

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicle.domain.LocateVehicle
import kotlinx.coroutines.launch
import timber.log.Timber

data class VehicleMapState(val location: Location? = null, val loading: Boolean = false)
class VehicleMapEvents

internal class VehicleMapViewModel @ViewModelInject constructor(private val locateVehicle: LocateVehicle) :
    UniDirectionalViewModel<VehicleMapState, VehicleMapEvents>(VehicleMapState()) {

    fun refreshPosition(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            locateVehicle(vin).fold(
                { setState { copy(location = it) } },
                { Timber.e(it) }
            )
            setState { copy(loading = false) }
        }
    }

}