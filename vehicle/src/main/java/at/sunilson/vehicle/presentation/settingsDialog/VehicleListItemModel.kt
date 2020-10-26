package at.sunilson.vehicle.presentation.settingsDialog

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class VehicleListItemModel : EpoxyModelWithHolder<VehicleListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_list_item

    @EpoxyAttribute
    lateinit var vehicle: Vehicle

    @EpoxyAttribute
    lateinit var onVehicleClick: (String) -> Unit

    override fun bind(holder: Holder) = holder.run {
        image.load(vehicle.imageUrl)
        vehicleInfo.text = vehicle.vin
        vehicleName.text = vehicle.modelName
        container.setOnClickListener { onVehicleClick(vehicle.vin) }
    }

    class Holder : KotlinEpoxyHolder() {
        val container by bind<ViewGroup>(R.id.container)
        val image by bind<ImageView>(R.id.vehicle_image)
        val vehicleName by bind<TextView>(R.id.vehicle_name)
        val vehicleInfo by bind<TextView>(R.id.vehicle_info)
    }
}