package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import at.sunilson.entities.Vehicle
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import at.sunilson.vehicle.presentation.extensions.displayName
import at.sunilson.vehicle.presentation.utils.TimeUtils
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class BatteryStatusWidgetModel : EpoxyModelWithHolder<BatteryStatusWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.battery_status_widget

    @EpoxyAttribute
    lateinit var batteryStatus: Vehicle.BatteryStatus

    override fun bind(holder: Holder) {
        val context = holder.batteryTemperature.context

        val chargeStateText =
            if (batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGING) {
                "${context.getString(batteryStatus.chargeState.displayName)}: ${TimeUtils.formatMinuteDuration(
                    batteryStatus.remainingChargeTime
                )} verbleibend"
            } else {
                holder.chargeStateView.context.getString(batteryStatus.chargeState.displayName) + "${batteryStatus.batteryCapacity}"
            }
        holder.chargeStateView.text = chargeStateText
        holder.pluggedStateView.text =
            if (batteryStatus.pluggedIn) "Ladekabel angesteckt. (${batteryStatus.chargeSpeed} kWh)" else "Ladekabel nicht angesteckt"
        holder.batteryTemperature.text =
            "Batterie Temperatur ist ${batteryStatus.batteryTemperature} Grad"
        holder.estimatedRange.text =
            "Reichweite ${batteryStatus.remainingRange} km (${batteryStatus.availableEnery} kWh)"
    }

    class Holder : KotlinEpoxyHolder() {
        val pluggedStateView by bind<TextView>(R.id.vehicle_battery_plugged)
        val chargeStateView by bind<TextView>(R.id.vehicle_charge_state)
        val batteryTemperature by bind<TextView>(R.id.vehicle_battery_temperature)
        val estimatedRange by bind<TextView>(R.id.vehicle_estimated_range)
    }
}

