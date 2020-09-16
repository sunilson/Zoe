package at.sunilson.appointments.domain

import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetNearestAppointment @Inject constructor(private val getSelectedVehicle: GetSelectedVehicle) :
    FlowUseCase<Appointment?, Unit>() {

    @Inject
    internal lateinit var getAllAppointments: GetAllAppointments

    override fun run(params: Unit) = getSelectedVehicle(Unit).flatMapLatest {
        if (it != null) {
            getAllAppointments(it.vin).map { appointments ->
                appointments.firstOrNull { appointment -> appointment.upcoming }
            }
        } else {
            flowOf(null)
        }
    }
}