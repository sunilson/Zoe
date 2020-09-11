package at.sunilson.vehicleDetails.presentation.epoxy.models

import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicleDetails.R
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class DetailListEquipmentModel : EpoxyModelWithHolder<DetailListEquipmentModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_details_list_equipment_item

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute
    lateinit var item: VehicleDetailsEntry.Equipment

    override fun bind(holder: Holder) = holder.run {
        titleView.isVisible = title != null
        titleView.text = title
        bodyView.text = item.label
    }

    class Holder : KotlinEpoxyHolder() {
        val titleView by bind<TextView>(R.id.title)
        val bodyView by bind<TextView>(R.id.body)
    }
}
