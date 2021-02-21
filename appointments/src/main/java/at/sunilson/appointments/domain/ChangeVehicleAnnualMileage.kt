package at.sunilson.appointments.domain

import at.sunilson.appointments.data.AppointmentsService
import at.sunilson.appointments.data.models.MileagePatchBody
import at.sunilson.appointments.domain.entities.ChangeVehicleMileageParams
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class ChangeVehicleAnnualMileage @Inject constructor(
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao
) : AsyncUseCase<Unit, ChangeVehicleMileageParams>() {

    @Inject
    internal lateinit var appointmentsService: AppointmentsService

    override suspend fun run(params: ChangeVehicleMileageParams) =
        SuspendableResult.of<Unit, Exception> {
            appointmentsService.updateVehicleAnnualMileage(
                vehicleCoreRepository.kamereonAccountID,
                params.vin,
                listOf(MileagePatchBody(params.mileage))
            )

            vehicleDao.updateAnnualMileage(params.vin, params.mileage)
        }
}
