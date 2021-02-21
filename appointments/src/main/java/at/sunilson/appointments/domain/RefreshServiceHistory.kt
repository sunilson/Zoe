package at.sunilson.appointments.domain

import at.sunilson.appointments.data.AppointmentsService
import at.sunilson.appointments.data.ServicesDao
import at.sunilson.appointments.data.toDatabaseEntity
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class RefreshServiceHistory @Inject constructor(private val vehicleCoreRepository: VehicleCoreRepository) :
    AsyncUseCase<Unit, String>() {

    @Inject
    internal lateinit var appointmentsService: AppointmentsService

    @Inject
    internal lateinit var servicesDao: ServicesDao

    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val response =
            appointmentsService.getServiceHistory(vehicleCoreRepository.kamereonAccountID, params)
        servicesDao.insertAndDeleteServices(response.services.map { it.toDatabaseEntity(params) })
    }
}
