package at.sunilson.appointments.presentation.appointments

import android.widget.TextView
import at.sunilson.appointments.R
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
internal abstract class AppointmentListItemModel : EpoxyModelWithHolder<AppointmentListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.appointment_list_item

    @EpoxyAttribute
    lateinit var appointment: Appointment

    override fun bind(holder: Holder) = holder.run {
        title.text = appointment.label
        subTitle.text = "${appointment.date?.formatPattern("dd.MM.YYYY")}"
    }

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val subTitle by bind<TextView>(R.id.subtitle)
    }
}
