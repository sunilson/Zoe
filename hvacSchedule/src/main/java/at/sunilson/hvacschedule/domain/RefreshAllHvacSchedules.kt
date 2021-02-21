package at.sunilson.hvacschedule.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.hvacschedule.data.HvacScheduleDatabase
import at.sunilson.hvacschedule.data.HvacScheduleService
import at.sunilson.scheduleCore.data.toDatabaseEntity
import at.sunilson.scheduleCore.data.toEntity
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshAllHvacSchedules @Inject constructor(
    private val chargeScheduleService: HvacScheduleService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val scheduleDatabase: HvacScheduleDatabase
) : AsyncUseCase<List<Schedule>, String>() {

    override suspend fun run(params: String) =
        SuspendableResult.of<List<Schedule>, Exception> {
            chargeScheduleService.getChargingSchedule2(
                vehicleCoreRepository.kamereonAccountID,
                params
            )

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
                    scheduleDatabase.hvacScheduleDao()
                        .insertAndDeleteSchedules(chargeSchedules.map {
                            it.toDatabaseEntity(params)
                        })
                }
        }
}
