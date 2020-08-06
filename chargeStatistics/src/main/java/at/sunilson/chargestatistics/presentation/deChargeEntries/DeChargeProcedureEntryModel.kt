package at.sunilson.chargestatistics.presentation.deChargeEntries

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatFull
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class DeChargeProcedureEntryModel :
    EpoxyModelWithHolder<DeChargeProcedureEntryModel.Holder>() {

    override fun getDefaultLayout() = R.layout.charge_tracking_point_entry_item

    @EpoxyAttribute
    lateinit var chargingProcedure: DeChargingProcedure

    @EpoxyAttribute
    lateinit var onItemClick: (View) -> Unit

    override fun bind(holder: Holder) = holder.run {
        title.text =
            "${chargingProcedure.batteryLevelDifference} % entladen auf ${chargingProcedure.kmDifference} km (${chargingProcedure.energyLevelDifference} kWh)"

        subTitle.text =
            "Von ${chargingProcedure.startTime.formatFull()} bis ${chargingProcedure.endTime.formatFull()}"

        card.setOnClickListener { onItemClick(it) }
    }

    class Holder : KotlinEpoxyHolder() {
        val card by bind<CardView>(R.id.container)
        val title by bind<TextView>(R.id.charge_procedure_title)
        val subTitle by bind<TextView>(R.id.charge_procedure_info)
    }
}