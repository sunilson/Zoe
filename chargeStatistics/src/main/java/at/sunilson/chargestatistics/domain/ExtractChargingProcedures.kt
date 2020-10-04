package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class ExtractChargingProcedures @Inject constructor() :
    AsyncUseCase<List<ChargingProcedure>, List<ChargeTrackingPoint>>() {
    override suspend fun run(trackingPoints: List<ChargeTrackingPoint>) =
        SuspendableResult.of<List<ChargingProcedure>, Exception> {
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
                    prev.batteryStatus.batteryLevel + GetChargingProcedures.CHARGING_THRESHOLD < chargeTrackingPoint.batteryStatus.batteryLevel

                if (chargingProcedureOnGoing) {
                    if (!batteryLevelIncreased && !isCharging) {
                        //Stop and save procedure
                        result.add(
                            ChargingProcedure(
                                currentStartTrackingPoint!!.batteryStatus.batteryLevel,
                                chargeTrackingPoint.batteryStatus.batteryLevel,
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
}