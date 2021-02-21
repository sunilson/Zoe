package at.sunilson.vehicle.presentation.settingsDialog

import android.widget.Button
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class SettingsDialogButtonsModel :
    EpoxyModelWithHolder<SettingsDialogButtonsModel.Holder>() {

    override fun getDefaultLayout() = R.layout.settings_dialog_buttons

    @EpoxyAttribute
    lateinit var logoutClicked: () -> Unit

    @EpoxyAttribute
    lateinit var impressumClicked: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        logoutButton.setOnClickListener { logoutClicked() }
        impressumButton.setOnClickListener { impressumClicked() }
    }

    class Holder : KotlinEpoxyHolder() {
        val logoutButton by bind<Button>(R.id.logout)
        val impressumButton by bind<Button>(R.id.impressum)
    }
}
