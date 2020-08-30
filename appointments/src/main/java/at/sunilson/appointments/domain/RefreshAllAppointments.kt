package at.sunilson.appointments.domain

import at.sunilson.appointments.data.AppointmentsDao
import at.sunilson.appointments.data.AppointmentsService
import at.sunilson.appointments.data.toDatabaseEntity
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshAllAppointments @Inject constructor(
    private val appointmentsService: AppointmentsService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val appointmentsDao: AppointmentsDao
) : AsyncUseCase<Unit, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val response = appointmentsService.getAppointments(
            vehicleCoreRepository.kamereonAccountID,
            params
        )
        appointmentsDao.insertAppointments(response.maintenance.map {
            it.toDatabaseEntity(
                params,
                response.deliveryDate
            )
        })
    }
}