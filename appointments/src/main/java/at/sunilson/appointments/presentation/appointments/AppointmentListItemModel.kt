package at.sunilson.appointments.presentation.appointments

import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.appointments.R
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
internal abstract class AppointmentListItemModel :
    EpoxyModelWithHolder<AppointmentListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.appointment_list_item

    @EpoxyAttribute
    var headline: String? = null

    @EpoxyAttribute
    lateinit var appointment: Appointment

    @EpoxyAttribute
    lateinit var addToCalendar: (Appointment) -> Unit

    override fun bind(holder: Holder) = holder.run {
        title.text = appointment.label
        subTitle.text = "${appointment.date?.formatPattern("dd.MM.YYYY")}"
        calendarButton.isVisible = appointment.upcoming
        calendarButton.setOnClickListener { addToCalendar(appointment) }
        header.isVisible = headline != null
        header.text = headline
    }

    class Holder : KotlinEpoxyHolder() {
        val calendarButton by bind<Button>(R.id.calendar_button)
        val title by bind<TextView>(R.id.title)
        val subTitle by bind<TextView>(R.id.subtitle)
        val header by bind<TextView>(R.id.section_header)
    }
}
