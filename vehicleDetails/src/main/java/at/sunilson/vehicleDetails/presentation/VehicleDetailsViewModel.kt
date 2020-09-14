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

internal data class VehicleDetailsState(
    val loading: Boolean = false,
    val details: List<VehicleDetailsEntry> = listOf(),
    val searchedIndex: Int = -1
)

internal sealed class VehicleDetailsEvent
internal object RefreshFailed: VehicleDetailsEvent()
internal data class ScrollToPosition(val position: Int) : VehicleDetailsEvent()

internal class VehicleDetailsViewModel @ViewModelInject constructor(
    private val refreshVehicleDetails: RefreshVehicleDetails,
    private val getVehicleDetails: GetVehicleDetails
) : UniDirectionalViewModel<VehicleDetailsState, VehicleDetailsEvent>(VehicleDetailsState()) {

    private var job: Job? = null

    fun refreshDetails(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshVehicleDetails(vin).fold(
                {},
                { Timber.e(it) }
            )
            setState { copy(loading = false) }
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

    fun search(query: String) {
        getState { state ->
            val index = if (query.isEmpty()) {
                -1
            } else {
                state.details.indexOfFirst { entry ->
                    when (entry) {
                        is VehicleDetailsEntry.Image -> false
                        is VehicleDetailsEntry.Equipment -> entry.label.contains(
                            query,
                            ignoreCase = true
                        )
                        is VehicleDetailsEntry.Information -> entry.description.contains(
                            query,
                            ignoreCase = true
                        )
                    }
                }
            }

            setState { copy(searchedIndex = index) }
            if (index != -1) {
                sendEvent(ScrollToPosition(index))
            }
        }
    }
}