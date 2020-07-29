package at.sunilson.chargestatistics.presentation.deChargeEntries

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import java.time.format.DateTimeFormatter

@EpoxyModelClass
abstract class DeChargeProcedureEntryModel :
    EpoxyModelWithHolder<DeChargeProcedureEntryModel.Holder>() {

    override fun getDefaultLayout() = R.layout.charge_tracking_point_entry_item

    @EpoxyAttribute
    lateinit var chargingProcedure: DeChargingProcedure


    override fun bind(holder: Holder) = holder.run {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.YY hh:mm")

        title.text =
            "${chargingProcedure.batteryLevelDifference} % entladen auf ${chargingProcedure.kmDifference} km (${chargingProcedure.energyLevelDifference} kWh)"

        subTitle.text =
            "Von ${chargingProcedure.startTime.format(formatter)} bis ${chargingProcedure.endTime.format(
                formatter
            )}"
    }

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.charge_procedure_title)
        val subTitle by bind<TextView>(R.id.charge_procedure_info)
    }
}