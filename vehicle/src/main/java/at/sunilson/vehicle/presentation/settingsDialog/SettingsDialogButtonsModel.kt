package at.sunilson.vehicle.presentation.settingsDialog

import android.widget.ImageView
import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.api.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class SettingsDialogButtonsModel : EpoxyModelWithHolder<VehicleListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.settings_dialog_buttons

    @EpoxyAttribute
    lateinit var settingsClicked: () -> Unit

    @EpoxyAttribute
    lateinit var impressumClicked: () -> Unit

    override fun bind(holder: VehicleListItemModel.Holder) = holder.run {  }


    class Holder : KotlinEpoxyHolder() {}
}
