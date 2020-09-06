package at.sunilson.appointments.domain

import at.sunilson.appointments.data.AppointmentsDao
import at.sunilson.appointments.data.AppointmentsService
import at.sunilson.appointments.data.models.DatabaseAppointment
import at.sunilson.appointments.data.toDatabaseEntity
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.vehiclecore.domain.GetVehicle
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.time.LocalDate
import javax.inject.Inject

class RefreshAppointments @Inject constructor(
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val getVehicle: GetVehicle
) : AsyncUseCase<Unit, String>() {

    @Inject
    internal lateinit var appointmentsService: AppointmentsService

    @Inject
    internal lateinit var appointmentsDao: AppointmentsDao

    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {

        val currentVehicle = getVehicle(params).get() ?: error("No vehicle existent!")

        val response = appointmentsService.getAppointments(
            vehicleCoreRepository.kamereonAccountID,
            params,
            currentVehicle.annualMileage,
            LocalDate.now().formatPattern("YYYY-MM-dd")
        )

        val appointments = mutableListOf<DatabaseAppointment>()
        response.upcomingMaintenances.forEach {
            it.maintenances.forEach { appointments.add(it.toDatabaseEntity(params)) }
        }

        appointmentsDao.insertAppointments(appointments)
    }
}