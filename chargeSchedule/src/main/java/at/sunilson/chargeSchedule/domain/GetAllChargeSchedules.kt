package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDatabase
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.scheduleCore.data.toEntity
import at.sunilson.scheduleCore.domain.entities.Schedule
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllChargeSchedules @Inject constructor(private val scheduleDatabase: ChargeScheduleDatabase) :
    FlowUseCase<List<Schedule>, String>() {
    override fun run(params: String) =
        scheduleDatabase.chargeScheduleDao().getAllSchedulesForVehicle(params)
            .map { it.map { it.toEntity() } }
}
