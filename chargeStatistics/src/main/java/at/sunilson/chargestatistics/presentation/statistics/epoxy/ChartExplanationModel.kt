package at.sunilson.chargestatistics.presentation.statistics.epoxy

import at.sunilson.chargestatistics.R
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class ChartExplanationModel : EpoxyModelWithHolder<ChartExplanationModel.Holder>() {

    override fun bind(holder: Holder) {
    }

    override fun getDefaultLayout() = R.layout.chart_explanation_model

    class Holder : KotlinEpoxyHolder() {
    }
}