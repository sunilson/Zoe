package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDatabase
import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.scheduleCore.data.toDatabaseEntity
import at.sunilson.scheduleCore.data.toEntity
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshAllChargeSchedules @Inject constructor(
    private val chargeScheduleService: ChargeScheduleService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val scheduleDatabase: ChargeScheduleDatabase
) : AsyncUseCase<List<Schedule>, String>() {

    override suspend fun run(params: String) =
        SuspendableResult.of<List<Schedule>, Exception> {
            val result = chargeScheduleService.getChargingSchedule(
                vehicleCoreRepository.kamereonAccountID,
                params
            )

            val chargeType = when (result.data.attributes.mode) {
                "scheduled" -> ScheduleType.SCHEDULED
                else -> ScheduleType.ALWAYS
            }

            result.data.attributes.schedules.map { it.toEntity(chargeType) }
                .also { chargeSchedules ->
                    scheduleDatabase.chargeScheduleDao()
                        .insertAndDeleteSchedules(chargeSchedules.map {
                            it.toDatabaseEntity(params)
                        })
                }
        }
}