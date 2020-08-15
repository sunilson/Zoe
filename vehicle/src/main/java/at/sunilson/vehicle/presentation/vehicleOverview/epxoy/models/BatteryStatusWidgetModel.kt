package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.Button
import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import at.sunilson.vehicle.presentation.extensions.displayName
import at.sunilson.vehicle.presentation.utils.TimeUtils
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class BatteryStatusWidgetModel : EpoxyModelWithHolder<BatteryStatusWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.battery_status_widget

    @EpoxyAttribute
    lateinit var vehicle: Vehicle

    @EpoxyAttribute
    lateinit var chargeScheduleClicked: (String) -> Unit

    override fun bind(holder: Holder) = holder.run {
        val context = holder.batteryTemperature.context
        val batteryStatus = vehicle.batteryStatus

        val chargeStateText =
            if (batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGING) {
                "${context.getString(batteryStatus.chargeState.displayName)}: ${TimeUtils.formatMinuteDuration(
                    batteryStatus.remainingChargeTime
                )} verbleibend"
            } else {
                holder.chargeStateView.context.getString(batteryStatus.chargeState.displayName)
            }
        chargeStateView.text = chargeStateText
        pluggedStateView.setText(if (batteryStatus.pluggedIn) R.string.plugged_in else R.string.not_plugged_in)
        batteryTemperature.text =
            context.getString(R.string.battery_temperature, batteryStatus.batteryTemperature)
        estimatedRange.text =
            "Reichweite ${batteryStatus.remainingRange} km (${batteryStatus.availableEnery} kWh)"
        chargeScheduleButton.setOnClickListener { chargeScheduleClicked(vehicle.vin) }
    }

    class Holder : KotlinEpoxyHolder() {
        val pluggedStateView by bind<TextView>(R.id.vehicle_battery_plugged)
        val chargeStateView by bind<TextView>(R.id.vehicle_charge_state)
        val batteryTemperature by bind<TextView>(R.id.vehicle_battery_temperature)
        val estimatedRange by bind<TextView>(R.id.vehicle_estimated_range)
        val chargeScheduleButton by bind<Button>(R.id.charge_schedule_button)
    }
}

