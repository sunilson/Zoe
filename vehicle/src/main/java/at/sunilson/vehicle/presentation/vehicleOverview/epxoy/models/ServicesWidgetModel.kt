package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.vehicle.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton

@EpoxyModelClass
abstract class ServicesWidgetModel :
    EpoxyModelWithHolder<ServicesWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.services_widget

    @EpoxyAttribute
    var appointment: Appointment? = null

    @EpoxyAttribute
    lateinit var allAppointmentsClicked: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        appointmentSubtitle.isVisible = appointment != null
        appointmentSubtitle.text = appointment?.date?.formatPattern("dd.MM.YYYY")

        appointmentTitle.text = if (appointment != null) {
            "Nächster Service: ${appointment?.label}"
        } else {
            "Kein anstehender Termin"
        }

        allAppointmentsButton.setOnClickListener { allAppointmentsClicked() }
    }

    class Holder : KotlinEpoxyHolder() {
        val appointmentTitle by bind<TextView>(R.id.next_appointment_title)
        val appointmentSubtitle by bind<TextView>(R.id.next_appointment_subtitle)
        val allAppointmentsButton by bind<MaterialButton>(R.id.all_appointments_button)
    }
}