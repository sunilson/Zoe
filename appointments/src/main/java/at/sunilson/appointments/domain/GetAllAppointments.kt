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
            val realAppointments = mutableListOf<Appointment>()
            databaseAppointments.forEach { dbAppointment ->
                dbAppointment.years.forEach { year ->
                    realAppointments.add(
                        Appointment(
                            try {
                                LocalDate.parse(dbAppointment.startDate).plusYears(year.toLong())
                            } catch (exception: Exception) {
                                null
                            },
                            dbAppointment.label
                        )
                    )
                }
            }

            realAppointments.filter { it.date != null }.sortedBy { it.date }
        }
}