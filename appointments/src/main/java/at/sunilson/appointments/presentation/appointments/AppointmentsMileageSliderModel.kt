package at.sunilson.appointments.presentation.appointments

import android.widget.TextView
import at.sunilson.appointments.R
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.slider.Slider

@EpoxyModelClass
internal abstract class AppointmentsMileageSliderModel :
    EpoxyModelWithHolder<AppointmentsMileageSliderModel.Holder>() {

    override fun getDefaultLayout() = R.layout.mileage_slider_item

    @EpoxyAttribute
    var annualMileage: Int = 10000

    @EpoxyAttribute(hash = false)
    lateinit var sliderValueChanged: (Int) -> Unit

    private var previousValue = 0

    override fun bind(holder: Holder) = holder.run {
        mileageText.text = "$annualMileage km"
        slider.value = annualMileage.toFloat()
        slider.clearOnSliderTouchListeners()
        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                val newValue = slider.value.toInt()
                if (newValue != previousValue) {
                    previousValue = newValue
                    sliderValueChanged(newValue)
                }
            }
        })
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.slider.clearOnSliderTouchListeners()
    }

    class Holder : KotlinEpoxyHolder() {
        val slider by bind<Slider>(R.id.slider)
        val mileageText by bind<TextView>(R.id.mileage)
    }
}