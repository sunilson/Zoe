package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetChargingProcedures @Inject constructor(
    private val getChargingPoints: GetAllChargeTrackingPoints,
    private val extractChargingProcedures: ExtractChargingProcedures
) : FlowUseCase<List<ChargingProcedure>, String>() {
    override fun run(params: String) = getChargingPoints(params)
        .map { trackingPoints -> extractChargingProcedures(trackingPoints).get() }

    companion object {
        const val CHARGING_THRESHOLD = 1
    }
}