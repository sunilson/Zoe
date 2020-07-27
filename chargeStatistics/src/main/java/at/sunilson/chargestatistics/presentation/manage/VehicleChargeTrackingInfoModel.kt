package at.sunilson.chargestatistics.presentation.manage

import android.widget.Switch
import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
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
        trackingState.text = "Tracker Status: ${vehicleChargeTrackingInfo.chargeTracker?.state}"
        toggle.isChecked = vehicleChargeTrackingInfo.chargeTracker.isTracking
        toggle.setOnClickListener {
            toggle.isChecked = vehicleChargeTrackingInfo.chargeTracker.isTracking
            toggleTracking(!vehicleChargeTrackingInfo.chargeTracker.isTracking)
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val toggle by bind<SwitchMaterial>(R.id.charge_tacking_toggle)
        val vehicleName by bind<TextView>(R.id.vehicle_name)
        val trackingState by bind<TextView>(R.id.tracking_state)
    }
}
