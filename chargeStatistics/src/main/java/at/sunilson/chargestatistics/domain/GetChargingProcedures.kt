package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

internal class GetChargingProcedures @Inject constructor(private val getChargingPoints: GetAllChargeTrackingPoints) :
    FlowUseCase<List<ChargingProcedure>, String>() {

    override fun run(params: String) = getChargingPoints(params).map { trackingPoints ->
        val result = mutableListOf<ChargingProcedure>()

        var previousTrackingPoint: ChargeTrackingPoint? = null

        var currentStartTrackingPoint: ChargeTrackingPoint? = null
        var currentEndTrackingPoint: ChargeTrackingPoint? = null

        trackingPoints.forEach { chargeTrackingPoint ->
            val prev = previousTrackingPoint ?: return@forEach

            if (prev.batteryStatus.batteryLevel >= chargeTrackingPoint.batteryStatus.batteryLevel) {
                // Finish current charging procedure if available
                if (currentStartTrackingPoint != null && currentEndTrackingPoint != null) {
                    result.add(
                        ChargingProcedure(
                            currentEndTrackingPoint!!.batteryStatus.batteryLevel - currentStartTrackingPoint!!.batteryStatus.batteryLevel,
                            currentEndTrackingPoint!!.batteryStatus.availableEnery - currentStartTrackingPoint!!.batteryStatus.availableEnery,
                            Instant
                                .ofEpochMilli(currentStartTrackingPoint!!.timestamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime(),
                            Instant
                                .ofEpochMilli(currentEndTrackingPoint!!.timestamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                        )
                    )

                    currentStartTrackingPoint = null
                    currentEndTrackingPoint = null
                }
            } else {
                // Start charging procedure or append to current one
                if (currentStartTrackingPoint == null) {
                    currentStartTrackingPoint = previousTrackingPoint
                }

                currentEndTrackingPoint = chargeTrackingPoint
            }

            previousTrackingPoint = chargeTrackingPoint
        }

        result
    }
}