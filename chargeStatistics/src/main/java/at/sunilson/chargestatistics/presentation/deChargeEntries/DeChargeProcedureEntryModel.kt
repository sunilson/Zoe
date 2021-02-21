package at.sunilson.chargestatistics.presentation.deChargeEntries

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
@Suppress("MaxLineLength", "MaximumLineLength")
abstract class DeChargeProcedureEntryModel :
    EpoxyModelWithHolder<DeChargeProcedureEntryModel.Holder>() {

    override fun getDefaultLayout() = R.layout.charge_tracking_point_entry_item

    @EpoxyAttribute
    lateinit var chargingProcedure: DeChargingProcedure

    @EpoxyAttribute
    var sectionHeader: String? = null

    @EpoxyAttribute(hash = false)
    lateinit var onItemClick: (View) -> Unit

    override fun bind(holder: Holder) = holder.run {
        this.sectionHeader.isVisible = this@DeChargeProcedureEntryModel.sectionHeader != null
        this.sectionHeader.text = this@DeChargeProcedureEntryModel.sectionHeader
        battery.text =
            "${chargingProcedure.batteryLevelDifference} % entladen (${chargingProcedure.startBatteryLevel} --> ${chargingProcedure.endBatteryLevel})"
        energy.text =
            "${chargingProcedure.energyLevelDifference} kWh auf ${chargingProcedure.kmDifference} km"
        from.text =
            "${chargingProcedure.startTime.formatPattern("dd.MM HH:mm")} bis ${
                chargingProcedure.endTime.formatPattern("dd.MM HH:mm")
            }"
    }

    class Holder : KotlinEpoxyHolder() {
        val from by bind<TextView>(R.id.charge_procedure_from)
        val battery by bind<TextView>(R.id.charge_procedure_battery)
        val energy by bind<TextView>(R.id.charge_procedure_energy)
        val sectionHeader by bind<TextView>(R.id.section_header)
    }
}
