package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetDeChargingProcedures @Inject constructor(
    private val getChargingPoints: GetAllChargeTrackingPoints,
    private val extractDeChargingProcedures: ExtractDeChargingProcedures
) : FlowUseCase<List<DeChargingProcedure>, String>() {

    override fun run(params: String) = getChargingPoints(params).map { trackingPoints ->
        extractDeChargingProcedures(trackingPoints).get()
    }
}