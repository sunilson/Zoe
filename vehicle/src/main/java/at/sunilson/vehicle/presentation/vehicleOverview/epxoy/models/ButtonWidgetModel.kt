package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton

@EpoxyModelClass
abstract class ButtonWidgetModel : EpoxyModelWithHolder<ButtonWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.button_widget

    @EpoxyAttribute
    lateinit var buttonText: String

    @EpoxyAttribute
    lateinit var onClick: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        button.setOnClickListener { onClick() }
        button.text = buttonText
    }


    class Holder : KotlinEpoxyHolder() {
        val button by bind<MaterialButton>(R.id.button)
    }
}
