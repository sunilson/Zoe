package at.sunilson.vehicle.presentation.settingsDialog

import android.widget.Button
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
abstract class SettingsDialogButtonsModel : EpoxyModelWithHolder<SettingsDialogButtonsModel.Holder>() {

    override fun getDefaultLayout() = R.layout.settings_dialog_buttons

    @EpoxyAttribute
    lateinit var settingsClicked: () -> Unit

    @EpoxyAttribute
    lateinit var impressumClicked: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        settingsButton.setOnClickListener { settingsClicked() }
    }


    class Holder : KotlinEpoxyHolder() {
        val settingsButton by bind<Button>(R.id.open_settings)
    }
}
