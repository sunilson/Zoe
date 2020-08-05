package at.sunilson.chargestatistics.presentation.chargeEntries

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatFull
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
        title.text =
            "${chargingProcedure.batteryLevelDifference} % geladen (${chargingProcedure.energyLevelDifference} kWh)"

        subTitle.text =
            "Von ${chargingProcedure.startTime.formatFull()} bis ${chargingProcedure.endTime.formatFull()}"
    }

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.charge_procedure_title)
        val subTitle by bind<TextView>(R.id.charge_procedure_info)
    }
}
