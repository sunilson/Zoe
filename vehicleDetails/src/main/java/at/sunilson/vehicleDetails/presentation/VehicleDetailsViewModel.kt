package at.sunilson.vehicleDetails.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicleDetails.domain.GetVehicleDetails
import at.sunilson.vehicleDetails.domain.RefreshVehicleDetails
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

data class VehicleDetailsState(val details: List<VehicleDetailsEntry> = listOf())
class VehicleDetailsEvent

internal class VehicleDetailsViewModel @ViewModelInject constructor(
    private val refreshVehicleDetails: RefreshVehicleDetails,
    private val getVehicleDetails: GetVehicleDetails
) : UniDirectionalViewModel<VehicleDetailsState, VehicleDetailsEvent>(VehicleDetailsState()) {

    private var job: Job? = null

    fun refreshDetails(vin: String) {
        viewModelScope.launch {
            refreshVehicleDetails(vin).fold(
                {},
                { Timber.e(it) }
            )
        }
    }

    fun loadVehicle(vin: String) {
        job?.cancel()
        job = viewModelScope.launch {
            getVehicleDetails(vin).collect {
                setState { copy(details = it) }
            }
        }
    }
}