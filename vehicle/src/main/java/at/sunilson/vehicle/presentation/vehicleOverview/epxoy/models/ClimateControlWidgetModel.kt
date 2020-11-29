package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class ClimateControlWidgetModel :
    EpoxyModelWithHolder<ClimateControlWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.climate_control_widget

    @EpoxyAttribute(hash = false)
    lateinit var startClimateControlClicked: () -> Unit

    @EpoxyAttribute(hash = false)
    lateinit var stopClimateControlClicked: () -> Unit

    @EpoxyAttribute(hash = false)
    lateinit var planClimateControlClicked: () -> Unit

    override fun bind(holder: Holder) {
        holder.startHVACButton.setOnClickListener { startClimateControlClicked() }
        holder.stopHVACButton.setOnClickListener { stopClimateControlClicked() }
        holder.planHVACButton.setOnClickListener { planClimateControlClicked() }
    }

    class Holder : KotlinEpoxyHolder() {
        val startHVACButton by bind<TextView>(R.id.start_climate_control_button)
        val stopHVACButton by bind<TextView>(R.id.stop_climate_control_button)
        val planHVACButton by bind<TextView>(R.id.plan_climate_control_button)
    }
}