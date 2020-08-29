package at.sunilson.vehiclecore.presentation.extensions

import androidx.annotation.StringRes
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehiclecore.R

val Vehicle.BatteryStatus.ChargeState.displayName: Int
    @StringRes
    get() = when(this) {
            Vehicle.BatteryStatus.ChargeState.NOT_CHARGING -> R.string.not_charging
            Vehicle.BatteryStatus.ChargeState.WATING_FOR_PLANNED_CHARGE -> R.string.waiting_for_planned_charge
            Vehicle.BatteryStatus.ChargeState.CHARGE_ENDED -> R.string.charge_ended
            Vehicle.BatteryStatus.ChargeState.WAITING_FOR_CURRENT_CHARGE -> R.string.waiting_for_current_charge
            Vehicle.BatteryStatus.ChargeState.ENERGY_FLAP_OPENED -> R.string.energy_flap_opened
            Vehicle.BatteryStatus.ChargeState.CHARGING -> R.string.charging
            Vehicle.BatteryStatus.ChargeState.CHARGE_ERROR -> R.string.charge_error
        }