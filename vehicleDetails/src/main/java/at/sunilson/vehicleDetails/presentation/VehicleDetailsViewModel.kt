package at.sunilson.vehicleDetails.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleDetails.domain.GetVehicle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class VehicleDetailsState(val vehicle: Vehicle? = null)
class VehicleDetailsEvent

internal class VehicleDetailsViewModel @ViewModelInject constructor(private val getVehicle: GetVehicle) :
    UniDirectionalViewModel<VehicleDetailsState, VehicleDetailsEvent>(VehicleDetailsState()) {

    fun loadVehicle(vin: String) {
        viewModelScope.launch {
            getVehicle(vin).collect {
                setState { copy(vehicle = it) }
            }
        }
    }
}