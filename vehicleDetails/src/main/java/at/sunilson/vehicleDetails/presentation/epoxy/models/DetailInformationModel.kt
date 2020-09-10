package at.sunilson.vehicleDetails.presentation.epoxy.models

import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicleDetails.R
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class DetailInformationModel : EpoxyModelWithHolder<DetailInformationModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_details_list_item

    @EpoxyAttribute
    lateinit var item: VehicleDetailsEntry.Information

    override fun bind(holder: Holder) = holder.run {
        titleView.text = item.title
        bodyView.text = item.description
    }

    class Holder : KotlinEpoxyHolder() {
        val titleView by bind<TextView>(R.id.title)
        val bodyView by bind<TextView>(R.id.body)
    }
}
