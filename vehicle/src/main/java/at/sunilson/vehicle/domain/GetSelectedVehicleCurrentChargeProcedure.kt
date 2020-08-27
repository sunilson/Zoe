package at.sunilson.vehicle.domain

import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.vehicle.domain.entities.ChargeProcedure
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

internal class GetSelectedVehicleCurrentChargeProcedure @Inject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val getAllChargeTrackingPoints: GetAllChargeTrackingPoints
) : FlowUseCase<ChargeProcedure?, Unit>() {
    override fun run(params: Unit) = getSelectedVehicle(Unit).flatMapLatest { vehicle ->
        if (vehicle == null) {
            flowOf()
        } else {
            getAllChargeTrackingPoints(vehicle.vin)
        }
    }.map { points ->
        val currentPoint = points.lastOrNull() ?: return@map null
        if (!currentPoint.batteryStatus.isCharging) {
            return@map null
        }

        val firstPoint =
            points.asReversed().firstOrNull() { !it.batteryStatus.isCharging } ?: return@map null

        ChargeProcedure(
            Duration.between(
                firstPoint.timestamp.toZonedDateTime(),
                currentPoint.timestamp.toZonedDateTime()
            ), currentPoint.batteryStatus.availableEnery - firstPoint.batteryStatus.availableEnery
        )
    }
}