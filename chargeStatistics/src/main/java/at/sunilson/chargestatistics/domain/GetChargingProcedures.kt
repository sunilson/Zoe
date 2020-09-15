package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject

internal class GetChargingProcedures @Inject constructor(private val getChargingPoints: GetAllChargeTrackingPoints) :
    FlowUseCase<List<ChargingProcedure>, String>() {

    override fun run(params: String) = getChargingPoints(params).map { trackingPoints ->
        val result = mutableListOf<ChargingProcedure>()
        var previousTrackingPoint: ChargeTrackingPoint? = null
        var currentStartTrackingPoint: ChargeTrackingPoint? = null

        trackingPoints.sortedBy { it.timestamp }.forEachIndexed { index, chargeTrackingPoint ->
            val prev = if (previousTrackingPoint == null) {
                previousTrackingPoint = chargeTrackingPoint
                return@forEachIndexed
            } else {
                previousTrackingPoint!!
            }

            val isCharging =
                chargeTrackingPoint.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGING
            val chargingProcedureOnGoing = currentStartTrackingPoint != null
            val batteryLevelIncreased =
                prev.batteryStatus.batteryLevel + CHARGING_THRESHOLD < chargeTrackingPoint.batteryStatus.batteryLevel

            if (chargingProcedureOnGoing) {
                if (!batteryLevelIncreased && !isCharging) {
                    //Stop and save procedure
                    result.add(
                        ChargingProcedure(
                            chargeTrackingPoint.batteryStatus.batteryLevel - currentStartTrackingPoint!!.batteryStatus.batteryLevel,
                            chargeTrackingPoint.batteryStatus.availableEnery - currentStartTrackingPoint!!.batteryStatus.availableEnery,
                            currentStartTrackingPoint!!.timestamp.toZonedDateTime(),
                            chargeTrackingPoint.timestamp.toZonedDateTime()
                        )
                    )

                    //Reset current start and end value
                    currentStartTrackingPoint = null
                }
            } else if (batteryLevelIncreased || isCharging) {
                // Start charging procedure
                currentStartTrackingPoint = prev
            }

            previousTrackingPoint = chargeTrackingPoint
        }

        result.sortedByDescending { it.startTime }
    }

    companion object {
        const val CHARGING_THRESHOLD = 1
    }
}