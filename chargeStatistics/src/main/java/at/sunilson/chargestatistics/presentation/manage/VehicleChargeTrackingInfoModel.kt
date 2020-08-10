package at.sunilson.chargestatistics.presentation.manage

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.entities.ChargeTracker
import at.sunilson.chargetracking.domain.entities.isTracking
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.switchmaterial.SwitchMaterial

@EpoxyModelClass
abstract class VehicleChargeTrackingInfoModel :
    EpoxyModelWithHolder<VehicleChargeTrackingInfoModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_charge_tracking_info_item

    @EpoxyAttribute
    lateinit var vehicleChargeTrackingInfo: VehicleChargeTrackingInfo

    @EpoxyAttribute
    lateinit var toggleTracking: (Boolean) -> Unit

    override fun bind(holder: Holder) = holder.run {
        vehicleName.text = vehicleChargeTrackingInfo.vehicle.modelName
        trackingState.text =
            "Tracker Status: ${getTrackerStatusName(vehicleChargeTrackingInfo.chargeTracker?.state)}"
        toggle.isChecked = vehicleChargeTrackingInfo.chargeTracker.isTracking
        toggle.setOnClickListener {
            toggle.isChecked = vehicleChargeTrackingInfo.chargeTracker.isTracking
            toggleTracking(!vehicleChargeTrackingInfo.chargeTracker.isTracking)
        }
    }

    private fun getTrackerStatusName(state: ChargeTracker.State?) = when (state) {
        ChargeTracker.State.BLOCKED -> "Geblockt"
        ChargeTracker.State.WAITING -> "Auf Ausführung warten"
        ChargeTracker.State.WORKING -> "Wird ausgeführt"
        ChargeTracker.State.COMPLETED -> "Fertig"
        null -> "Nicht gestartet"
    }

    class Holder : KotlinEpoxyHolder() {
        val toggle by bind<SwitchMaterial>(R.id.charge_tacking_toggle)
        val vehicleName by bind<TextView>(R.id.vehicle_name)
        val trackingState by bind<TextView>(R.id.tracking_state)
    }
}
