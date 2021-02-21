package at.arkulpa.notifications.domain.entities

import at.sunilson.vehiclecore.domain.entities.Vehicle

data class CheckNotificationsParams(val vin: String, val batteryStatus: Vehicle.BatteryStatus)
