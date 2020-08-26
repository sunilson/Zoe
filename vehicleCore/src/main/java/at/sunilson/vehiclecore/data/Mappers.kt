package at.sunilson.vehiclecore.data

import at.sunilson.vehiclecore.data.models.DatabaseLocation
import at.sunilson.vehiclecore.data.models.DatabaseVehicle
import at.sunilson.vehiclecore.data.models.batterystatus.BatteryStatusResponse
import at.sunilson.vehiclecore.data.models.cockpit.CockpitResponse
import at.sunilson.vehiclecore.data.models.location.LocationResponse
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import java.time.ZonedDateTime

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

fun CockpitResponse.toEntity() = data.attributes.totalMileage

fun DatabaseVehicle.toEntity() = vehicle

fun LocationResponse.toEntity() =
    Location(
        data.attributes.gpsLatitude,
        data.attributes.gpsLongitude,
        ZonedDateTime
            .parse(data.attributes.lastUpdateTime)
            .toInstant()
            .toEpochMilli()
    )

fun Location.toDatabaseEntity(vin: String) = DatabaseLocation(vin, lat, lng, timestamp)
fun DatabaseLocation.toEntity() = Location(lat, lng, timestamp)