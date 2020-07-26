package at.sunilson.chargestatistics.presentation.manage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.chargestatistics.domain.GetVehiclesWithTrackerInfo
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.StartChargeTracking
import at.sunilson.chargetracking.domain.StopChargeTracking
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class ManageState(val trackingInfos: List<VehicleChargeTrackingInfo> = listOf())
sealed class ManageEvent
internal class ManageViewModel @ViewModelInject constructor(
    private val getVehiclesWithTrackerInfo: GetVehiclesWithTrackerInfo,
    private val startChargeTracking: StartChargeTracking,
    private val stopChargeTracking: StopChargeTracking
) : UniDirectionalViewModel<ManageState, ManageEvent>(ManageState()) {

    init {
        viewModelScope.launch {
            getVehiclesWithTrackerInfo(Unit).collect {
                setState { copy(trackingInfos = it) }
            }
        }
    }

    fun stopTracking(vin: String) {
        viewModelScope.launch {
            stopChargeTracking(vin).fold(
                {},
                {}
            )
        }
    }

    fun startTracking(vin: String) {
        startChargeTracking(vin).fold(
            {},
            {}
        )
    }
}