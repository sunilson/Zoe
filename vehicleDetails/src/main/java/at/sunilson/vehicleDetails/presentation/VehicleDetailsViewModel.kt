package at.sunilson.vehicleDetails.presentation

import androidx.lifecycle.ViewModel
import at.sunilson.vehicleDetails.domain.GetVehicleDetails
import at.sunilson.vehicleDetails.domain.RefreshVehicleDetails
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

internal data class VehicleDetailsState(
    val loading: Boolean = false,
    val showSearch: Boolean = false,
    val details: List<VehicleDetailsEntry> = listOf(),
    val searchedIndex: Int = -1
)

internal sealed class VehicleDetailsSideEffects {
    object RefreshFailed : VehicleDetailsSideEffects()
    data class ScrollToPosition(val position: Int) : VehicleDetailsSideEffects()
}

@HiltViewModel
internal class VehicleDetailsViewModel @Inject constructor(
    private val refreshVehicleDetails: RefreshVehicleDetails,
    private val getVehicleDetails: GetVehicleDetails
) : ViewModel(), ContainerHost<VehicleDetailsState, VehicleDetailsSideEffects> {

    override val container = container<VehicleDetailsState, VehicleDetailsSideEffects>(
        VehicleDetailsState()
    )

    fun viewCreated(vin: String) = intent {
        getVehicleDetails(vin).collect {
            reduce { state.copy(details = it) }
        }
    }

    fun refreshDetailsRequested(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshVehicleDetails(vin).fold(
            {},
            { Timber.e(it) }
        )
        reduce { state.copy(loading = false) }
    }

    fun searchButtonClicked() = intent {
        reduce { state.copy(showSearch = !state.showSearch) }
    }

    fun searchQueryEntered(query: String) = intent {
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

        reduce { state.copy(searchedIndex = index) }
        if (index != -1) {
            postSideEffect(VehicleDetailsSideEffects.ScrollToPosition(index))
        }
    }
}
