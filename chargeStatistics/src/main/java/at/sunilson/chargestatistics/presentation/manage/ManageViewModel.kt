package at.sunilson.chargestatistics.presentation.manage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import at.sunilson.chargestatistics.domain.GetVehiclesWithTrackerInfo
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.StartChargeTracking
import at.sunilson.chargetracking.domain.StopChargeTracking
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.coroutines.transformFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.strict.orbit
import org.orbitmvi.orbit.syntax.strict.reduce
import org.orbitmvi.orbit.viewmodel.container

data class ManageState(val trackingInfos: List<VehicleChargeTrackingInfo> = listOf())
sealed class ManageEvent
internal class ManageViewModel @ViewModelInject constructor(
    private val getVehiclesWithTrackerInfo: GetVehiclesWithTrackerInfo,
    private val startChargeTracking: StartChargeTracking,
    private val stopChargeTracking: StopChargeTracking
) : ViewModel(), ContainerHost<ManageState, ManageEvent> {

    override val container = container<ManageState, ManageEvent>(ManageState())

    init {
        orbit {
            transformFlow { getVehiclesWithTrackerInfo(Unit) }.reduce { state.copy(trackingInfos = event) }
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