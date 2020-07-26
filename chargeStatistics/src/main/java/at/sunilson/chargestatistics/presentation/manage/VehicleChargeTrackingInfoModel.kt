package at.sunilson.chargestatistics.presentation.manage

import android.widget.Switch
import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.VehicleChargeTrackingInfo
import at.sunilson.chargetracking.domain.entities.ChargeTracker
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class VehicleChargeTrackingInfoModel :
    EpoxyModelWithHolder<VehicleChargeTrackingInfoModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_charge_tracking_info_item

    @EpoxyAttribute
    lateinit var vehicleChargeTrackingInfo: VehicleChargeTrackingInfo

    @EpoxyAttribute
    lateinit var toggleTracking: (Boolean) -> Unit


    override fun bind(holder: Holder) = holder.run {
        val isTracking =
            vehicleChargeTrackingInfo.chargeTracker != null && vehicleChargeTrackingInfo.chargeTracker?.state != ChargeTracker.State.COMPLETED
        vehicleName.text =
            "${vehicleChargeTrackingInfo.vehicle.modelName} (${vehicleChargeTrackingInfo.chargeTracker?.state})"
        toggle.isChecked = isTracking
        toggle.setOnClickListener {
            toggle.isChecked = isTracking
            toggleTracking(!isTracking)
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val toggle by bind<Switch>(R.id.charge_tacking_toggle)
        val vehicleName by bind<TextView>(R.id.vehicle_name)
    }
}
