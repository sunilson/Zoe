package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class ClimateControlWidgetModel : EpoxyModelWithHolder<ClimateControlWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.climate_control_widget

    @EpoxyAttribute
    lateinit var startClimateControlClicked: () -> Unit

    override fun bind(holder: Holder) {
        holder.startHVACButton.setOnClickListener { startClimateControlClicked() }
    }

    class Holder : KotlinEpoxyHolder() {
        val startHVACButton by bind<TextView>(R.id.start_climate_control_button)
    }
}