package at.sunilson.contracts.presentation

import android.widget.TextView
import at.sunilson.contracts.R
import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class ContractListItemModel : EpoxyModelWithHolder<ContractListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.contract_list_item

    @EpoxyAttribute
    lateinit var contract: Contract

    override fun bind(holder: Holder) = holder.run {
        description.text = contract.description
        duration.text =
            "Läuft bis ${contract.endDate?.formatPattern("dd.MM.YYYY")} - ${contract.durationMonths} Monate"
        mileage.text = "Kilometer: " + if (contract.maximumMileage == null) {
            "Unlimitiert"
        } else {
            "${contract.maximumMileage} ${contract.mileageUnit}"
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val description by bind<TextView>(R.id.description)
        val duration by bind<TextView>(R.id.duration)
        val mileage by bind<TextView>(R.id.mileage)
    }
}