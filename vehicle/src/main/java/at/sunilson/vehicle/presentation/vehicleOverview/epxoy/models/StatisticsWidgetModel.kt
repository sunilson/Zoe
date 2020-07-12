package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton

@EpoxyModelClass
abstract class StatisticsWidgetModel : EpoxyModelWithHolder<StatisticsWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.statitisc_widget

    @EpoxyAttribute
    lateinit var onChargeClick: () -> Unit

    @EpoxyAttribute
    lateinit var onHvacClick: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        chargeButton.setOnClickListener { onChargeClick() }
        hvacButton.setOnClickListener { onHvacClick() }
    }

    class Holder : KotlinEpoxyHolder() {
        val chargeButton by bind<MaterialButton>(R.id.charging_statistics_button)
        val hvacButton by bind<MaterialButton>(R.id.hvac_statistics_button)
    }
}

