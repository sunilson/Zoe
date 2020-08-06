package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.chargeSchedule.data.toEntity
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshAllChargeSchedules @Inject constructor(
    private val chargeScheduleService: ChargeScheduleService,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<List<ChargeSchedule>, String>() {

    override suspend fun run(params: String) =
        SuspendableResult.of<List<ChargeSchedule>, Exception> {
            val result = chargeScheduleService.getChargingSchedule(
                vehicleCoreRepository.kamereonAccountID,
                params
            )
            result.data.attributes.schedules.map { it.toEntity() }
        }
}