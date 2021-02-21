package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class ExtractDeChargingProcedures @Inject constructor() :
    AsyncUseCase<List<DeChargingProcedure>, List<ChargeTrackingPoint>>() {
    override suspend fun run(trackingPoints: List<ChargeTrackingPoint>) =
        SuspendableResult.of<List<DeChargingProcedure>, Exception> {
            val result = mutableListOf<DeChargingProcedure>()
            var previousTrackingPoint: ChargeTrackingPoint? = null
            var currentStartTrackingPoint: ChargeTrackingPoint? = null

            trackingPoints.sortedBy { it.timestamp }.forEachIndexed { index, chargeTrackingPoint ->
                val prev = if (previousTrackingPoint == null) {
                    previousTrackingPoint = chargeTrackingPoint
                    return@forEachIndexed
                } else {
                    previousTrackingPoint!!
                }

                val currentAvailableEnergy =
                    currentStartTrackingPoint!!.batteryStatus.availableEnery
                val deChargingProcedureOnGoing = currentStartTrackingPoint != null
                val batteryLevelDecreased =
                    prev.batteryStatus.batteryLevel > chargeTrackingPoint.batteryStatus.batteryLevel

                if (deChargingProcedureOnGoing) {
                    if (!batteryLevelDecreased) {
                        // Stop and save procedure
                        result.add(
                            DeChargingProcedure(
                                currentStartTrackingPoint!!.batteryStatus.batteryLevel,
                                prev.batteryStatus.batteryLevel,
                                currentAvailableEnergy - prev.batteryStatus.availableEnery,
                                currentStartTrackingPoint!!.timestamp.toZonedDateTime(),
                                prev.timestamp.toZonedDateTime(),
                                chargeTrackingPoint.mileageKm - currentStartTrackingPoint!!.mileageKm
                            )
                        )

                        // Reset current start and end value
                        currentStartTrackingPoint = null
                    }
                } else if (batteryLevelDecreased) {
                    // Start charging procedure
                    currentStartTrackingPoint = prev
                }

                previousTrackingPoint = chargeTrackingPoint
            }

            result.sortedByDescending { it.startTime }
        }
}
