package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

internal class GetDeChargingProcedures @Inject constructor(private val getChargingPoints: GetAllChargeTrackingPoints) :
    FlowUseCase<List<DeChargingProcedure>, String>() {

    private val ChargeTrackingPoint.dateTime: LocalDateTime
        get() = Instant
            .ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

    override fun run(params: String) = getChargingPoints(params).map { trackingPoints ->
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

            val deChargingProcedureOnGoing = currentStartTrackingPoint != null
            val batteryLevelDecreased =
                prev.batteryStatus.batteryLevel > chargeTrackingPoint.batteryStatus.batteryLevel

            if (deChargingProcedureOnGoing) {
                if (!batteryLevelDecreased) {
                    //Stop and save procedure
                    result.add(
                        DeChargingProcedure(
                            currentStartTrackingPoint!!.batteryStatus.batteryLevel - chargeTrackingPoint.batteryStatus.batteryLevel,
                            currentStartTrackingPoint!!.batteryStatus.availableEnery - chargeTrackingPoint.batteryStatus.availableEnery,
                            currentStartTrackingPoint!!.dateTime,
                            chargeTrackingPoint.dateTime,
                            chargeTrackingPoint.mileageKm - currentStartTrackingPoint!!.mileageKm
                        )
                    )

                    //Reset current start and end value
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