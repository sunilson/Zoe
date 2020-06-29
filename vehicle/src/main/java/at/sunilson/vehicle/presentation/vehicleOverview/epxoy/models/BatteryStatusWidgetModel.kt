package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import at.sunilson.entities.Vehicle
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class BatteryStatusWidgetModel : EpoxyModelWithHolder<BatteryStatusWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.battery_status_widget

    @EpoxyAttribute
    lateinit var batteryStatus: Vehicle.BatteryStatus

    override fun bind(holder: Holder) {
        holder.chargeStateView.text =
            "${batteryStatus.chargeState} mit ${batteryStatus.chargeSpeed} kW"
        holder.pluggedStateView.text =
            if (batteryStatus.pluggedIn) "Ladekabel angesteckt" else "Ladekabel nicht angesteckt"
        holder.batteryTemperature.text =
            "Batterie Temperatur ist ${batteryStatus.batteryTemperature} Grad"
        holder.estimatedRange.text =
            "Reichweite sind ca. ${batteryStatus.remainingRange} Km"
    }

    class Holder : KotlinEpoxyHolder() {
        val pluggedStateView by bind<TextView>(R.id.vehicle_battery_plugged)
        val chargeStateView by bind<TextView>(R.id.vehicle_charge_state)
        val batteryTemperature by bind<TextView>(R.id.vehicle_battery_temperature)
        val estimatedRange by bind<TextView>(R.id.vehicle_estimated_range)
    }
}

