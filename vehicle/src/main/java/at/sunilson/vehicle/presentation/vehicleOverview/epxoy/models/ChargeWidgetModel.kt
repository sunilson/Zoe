package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.format
import at.sunilson.vehicle.R
import at.sunilson.vehicle.domain.entities.ChargeProcedure
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class ChargeWidgetModel : EpoxyModelWithHolder<ChargeWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.charge_widget

    @EpoxyAttribute
    lateinit var vehicle: Vehicle

    @EpoxyAttribute
    var currentChargeProcedure: ChargeProcedure? = null

    @EpoxyAttribute
    lateinit var chargeScheduleClicked: () -> Unit

    @EpoxyAttribute
    lateinit var chargeNowClicked: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        val context = holder.batteryTemperature.context
        val batteryStatus = vehicle.batteryStatus

        chargeStateIcon.isVisible = currentChargeProcedure != null
        chargeStateView.isVisible = currentChargeProcedure != null
        currentChargeProcedure?.let { procedure ->
            chargeStateView.text =
                "Seit ${procedure.duration.format()} ${procedure.energyAmount} kWh geladen"
        }

        pluggedStateView.setText(if (batteryStatus.pluggedIn) R.string.plugged_in else R.string.not_plugged_in)
        batteryTemperature.text =
            context.getString(R.string.battery_temperature, batteryStatus.batteryTemperature)
        chargeScheduleButton.setOnClickListener { chargeScheduleClicked() }

        chargeNowButton.setOnClickListener { chargeNowClicked() }
        chargeNowButton.isEnabled =
            !vehicle.batteryStatus.isCharging && vehicle.batteryStatus.pluggedIn
                    && (vehicle.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.NOT_CHARGING
                    || vehicle.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.WAITING_FOR_CURRENT_CHARGE
                    || vehicle.batteryStatus.chargeState == Vehicle.BatteryStatus.ChargeState.WATING_FOR_PLANNED_CHARGE
                    )


    }

    class Holder : KotlinEpoxyHolder() {
        val pluggedStateView by bind<TextView>(R.id.vehicle_battery_plugged)
        val chargeStateIcon by bind<TextView>(R.id.battery_icon)
        val chargeStateView by bind<TextView>(R.id.vehicle_charge_state)
        val batteryTemperature by bind<TextView>(R.id.vehicle_battery_temperature)
        val chargeScheduleButton by bind<Button>(R.id.charge_schedule_button)
        val chargeNowButton by bind<Button>(R.id.charge_now_button)
    }
}

