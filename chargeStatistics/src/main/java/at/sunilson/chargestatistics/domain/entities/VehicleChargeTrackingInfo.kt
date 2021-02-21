package at.sunilson.chargestatistics.domain.entities

import at.sunilson.chargetracking.domain.entities.ChargeTracker
import at.sunilson.vehiclecore.domain.entities.Vehicle

data class VehicleChargeTrackingInfo(val vehicle: Vehicle, val chargeTracker: ChargeTracker? = null)
