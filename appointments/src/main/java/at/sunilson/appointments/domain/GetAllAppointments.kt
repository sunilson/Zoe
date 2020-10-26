package at.sunilson.appointments.domain

import at.sunilson.appointments.data.AppointmentsDao
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

internal class GetAllAppointments @Inject constructor(private val appointmentsDao: AppointmentsDao) :
    FlowUseCase<List<Appointment>, String>() {
    override fun run(params: String) =
        appointmentsDao.getAllAppointments(params).map { databaseAppointments ->
            databaseAppointments.map { dbAppointment ->
                Appointment(
                    try {
                        LocalDate.parse(dbAppointment.date)
                    } catch (exception: Exception) {
                        LocalDate.now().minusDays(1)
                    },
                    dbAppointment.label
                )
            }.sortedBy { it.date }
        }
}