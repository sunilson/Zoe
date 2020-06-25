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
        holder.chargeStateView.text = "Ladestatus: ${batteryStatus.chargeState}"
        holder.pluggedStateView.text = "Ladekabel angesteckt: ${batteryStatus.pluggedIn}"
    }

    class Holder : KotlinEpoxyHolder() {
        val pluggedStateView by bind<TextView>(R.id.vehicle_battery_plugged)
        val chargeStateView by bind<TextView>(R.id.vehicle_charge_state)
    }
}

