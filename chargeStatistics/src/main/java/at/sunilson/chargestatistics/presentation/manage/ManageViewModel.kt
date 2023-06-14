package at.sunilson.chargestatistics.presentation.manage

import androidx.lifecycle.ViewModel
import at.sunilson.chargestatistics.domain.GetVehiclesWithTrackerInfo
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.StartChargeTracking
import at.sunilson.chargetracking.domain.StopChargeTracking
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class ManageState(val trackingInfos: List<VehicleChargeTrackingInfo> = listOf())
sealed class ManageEvent

@HiltViewModel
internal class ManageViewModel @Inject constructor(
    private val getVehiclesWithTrackerInfo: GetVehiclesWithTrackerInfo,
    private val startChargeTracking: StartChargeTracking,
    private val stopChargeTracking: StopChargeTracking
) : ViewModel(), ContainerHost<ManageState, ManageEvent> {

    override val container = container<ManageState, ManageEvent>(ManageState())

    init {
        intent {
            getVehiclesWithTrackerInfo(Unit).collect {
                reduce { state.copy(trackingInfos = it) }
            }
        }
    }

    fun stopTrackingClicked(vin: String) = intent {
        stopChargeTracking(vin).fold(
            {},
            {}
        )
    }

    fun startTrackingClicked(vin: String) = intent {
        startChargeTracking(vin).fold(
            {},
            {}
        )
    }
}
