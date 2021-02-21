package at.sunilson.chargestatistics.presentation.statistics.epoxy

import android.widget.TextView
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class FactModel : EpoxyModelWithHolder<FactModel.Holder>() {

    @EpoxyAttribute
    lateinit var fact: Statistic.Fact

    override fun bind(holder: Holder) {
        holder.label.text = fact.label
        holder.value.text = fact.value
    }

    override fun getDefaultLayout() = R.layout.fact_model

    class Holder : KotlinEpoxyHolder() {
        val label by bind<TextView>(R.id.label)
        val value by bind<TextView>(R.id.value)
    }
}
