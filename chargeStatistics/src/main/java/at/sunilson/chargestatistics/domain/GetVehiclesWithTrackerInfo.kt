package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.GetRunningChargeTrackers
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.domain.GetAllVehicles
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetVehiclesWithTrackerInfo @Inject constructor(
    private val getAllVehicles: GetAllVehicles,
    private val getRunningChargeTrackers: GetRunningChargeTrackers
) : FlowUseCase<List<VehicleChargeTrackingInfo>, Unit>() {
    override fun run(params: Unit) =
        getAllVehicles(Unit).flatMapMerge { vehicles ->
            getRunningChargeTrackers(vehicles.map { it.vin }).map { chargeTrackers ->
                vehicles.map { vehicle ->
                    VehicleChargeTrackingInfo(
                        vehicle,
                        chargeTrackers.firstOrNull { it.vin == vehicle.vin }
                    )
                }
            }
        }
}