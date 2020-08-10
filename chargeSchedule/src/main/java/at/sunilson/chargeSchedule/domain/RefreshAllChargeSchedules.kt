package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDao
import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.chargeSchedule.data.toDatabaseEntity
import at.sunilson.chargeSchedule.data.toEntity
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import at.sunilson.chargeSchedule.domain.entities.ChargeType
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshAllChargeSchedules @Inject constructor(
    private val chargeScheduleService: ChargeScheduleService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val chargeScheduleDao: ChargeScheduleDao
) : AsyncUseCase<List<ChargeSchedule>, String>() {

    override suspend fun run(params: String) =
        SuspendableResult.of<List<ChargeSchedule>, Exception> {
            val result = chargeScheduleService.getChargingSchedule(
                vehicleCoreRepository.kamereonAccountID,
                params
            )

            val chargeType = when (result.data.attributes.mode) {
                "scheduled" -> ChargeType.SCHEDULED
                else -> ChargeType.ALWAYS
            }

            result.data.attributes.schedules.map { it.toEntity(chargeType) }
                .also { chargeSchedules ->
                    chargeScheduleDao.insertChargeSchedules(chargeSchedules.map {
                        it.toDatabaseEntity(params)
                    })
                }
        }
}