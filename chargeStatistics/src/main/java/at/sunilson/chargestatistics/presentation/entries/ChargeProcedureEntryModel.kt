package at.sunilson.chargestatistics.presentation.entries

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class ChargeProcedureEntryModel :
    EpoxyModelWithHolder<ChargeProcedureEntryModel.Holder>() {

    override fun getDefaultLayout() = R.layout.charge_tracking_point_entry_item

    @EpoxyAttribute
    lateinit var chargingProcedure: ChargingProcedure


    override fun bind(holder: Holder) = holder.run {
        timestamp.text =
            "Charged ${chargingProcedure.batteryLevelDifference} % (${chargingProcedure.energyLevelDifference} kWh) from ${chargingProcedure.startTime} to ${chargingProcedure.endTime}"
    }

    class Holder : KotlinEpoxyHolder() {
        val timestamp by bind<TextView>(R.id.timestamp)
    }
}
