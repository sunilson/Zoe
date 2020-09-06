package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.contracts.domain.entities.Contract
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
    var contract: Contract? = null

    @EpoxyAttribute
    lateinit var allAppointmentsClicked: () -> Unit

    @EpoxyAttribute
    lateinit var allContractsClicked: () -> Unit

    override fun bind(holder: Holder) = holder.run {
        appointmentSubtitle.isVisible = appointment != null
        appointmentSubtitle.text = appointment?.date?.formatPattern("dd.MM.YYYY")

        contractSubtitle.isVisible = contract != null
        contractSubtitle.text = contract?.endDate?.formatPattern("dd.MM.YYYY")

        appointmentTitle.text = if (appointment != null) {
            "Nächster Service: ${appointment?.label}"
        } else {
            "Kein anstehender Termin"
        }

        contractTitle.text = if (contract != null) {
            "Nächster Vertrag: ${contract?.description}"
        } else {
            "Kein Vertrag vorhanden"
        }

        allAppointmentsButton.setOnClickListener { allAppointmentsClicked() }
        allContractsButton.setOnClickListener { allContractsClicked() }
    }

    class Holder : KotlinEpoxyHolder() {
        val contractTitle by bind<TextView>(R.id.next_contract_title)
        val contractSubtitle by bind<TextView>(R.id.next_contract_subtitle)
        val appointmentTitle by bind<TextView>(R.id.next_appointment_title)
        val appointmentSubtitle by bind<TextView>(R.id.next_appointment_subtitle)
        val allAppointmentsButton by bind<MaterialButton>(R.id.all_appointments_button)
        val allContractsButton by bind<MaterialButton>(R.id.all_contracts_button)
    }
}