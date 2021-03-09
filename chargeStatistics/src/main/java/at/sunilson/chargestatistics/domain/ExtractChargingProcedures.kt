package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.GetChargingProcedures.Companion.CHARGING_THRESHOLD
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.vehiclecore.domain.entities.Vehicle.BatteryStatus.ChargeState.CHARGING
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class ExtractChargingProcedures @Inject constructor() :
    AsyncUseCase<List<ChargingProcedure>, List<ChargeTrackingPoint>>() {
    override suspend fun run(trackingPoints: List<ChargeTrackingPoint>) =
        SuspendableResult.of<List<ChargingProcedure>, Exception> {
            val result = mutableListOf<ChargingProcedure>()
            var previousTrackingPoint: ChargeTrackingPoint? = null
            var currentStartTrackingPoint: ChargeTrackingPoint? = null

            trackingPoints.sortedBy { it.timestamp }.forEachIndexed { index, cp ->
                val prev = if (previousTrackingPoint == null) {
                    previousTrackingPoint = cp
                    return@forEachIndexed
                } else {
                    previousTrackingPoint!!
                }

                val prevBatteryLevel = prev.batteryStatus.batteryLevel
                val availableEnergy = cp.batteryStatus.availableEnery
                val batteryLevel = cp.batteryStatus.batteryLevel
                val isCharging = cp.batteryStatus.chargeState == CHARGING
                val chargingProcedureOnGoing = currentStartTrackingPoint != null
                val batteryLevelIncreased = prevBatteryLevel + CHARGING_THRESHOLD < batteryLevel

                if (chargingProcedureOnGoing) {
                    if (!batteryLevelIncreased && !isCharging) {
                        val currentBatteryLevel =
                            currentStartTrackingPoint!!.batteryStatus.batteryLevel
                        val currentAvailableEnergy =
                            currentStartTrackingPoint!!.batteryStatus.availableEnery

                        // Stop and save procedure
                        result.add(
                            ChargingProcedure(
                                currentBatteryLevel,
                                batteryLevel,
                                availableEnergy - currentAvailableEnergy,
                                currentStartTrackingPoint!!.timestamp.toZonedDateTime(),
                                cp.timestamp.toZonedDateTime()
                            )
                        )

                        // Reset current start and end value
                        currentStartTrackingPoint = null
                    }
                } else if (batteryLevelIncreased || isCharging) {
                    // Start charging procedure
                    currentStartTrackingPoint = prev
                }

                previousTrackingPoint = cp
            }

            result.sortedByDescending { it.startTime }
        }
}
