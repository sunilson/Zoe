package at.sunilson.vehiclecore.data

import at.sunilson.vehiclecore.data.models.DatabaseVehicle
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse

fun BatteryStatusResponse.toEntity() =
    Vehicle.BatteryStatus(
        data.attributes.batteryLevel,
        data.attributes.batteryTemperature,
        data.attributes.batteryAutonomy,
        data.attributes.batteryAvailableEnergy,
        data.attributes.plugStatus == 1,
        Vehicle.BatteryStatus.ChargeState.values()
            .firstOrNull { it.stateCode == data.attributes.chargingStatus }
            ?: Vehicle.BatteryStatus.ChargeState.NOT_CHARGING,
        data.attributes.chargingInstantaneousPower,
        data.attributes.chargingRemainingTime
    )

fun Vehicle.toDatabaseEntity() = DatabaseVehicle(vin, this)

fun DatabaseVehicle.toEntity() = vehicle