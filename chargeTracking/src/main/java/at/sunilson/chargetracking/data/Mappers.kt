package at.sunilson.chargetracking.data

import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.chargetracking.data.models.ChargeTrackingPoint as DatabaseChargeTrackingPoint

internal fun ChargeTrackingPoint.toDatabaseEntity() =
    DatabaseChargeTrackingPoint(vehicleId, timestamp, batteryStatus, mileageKm)