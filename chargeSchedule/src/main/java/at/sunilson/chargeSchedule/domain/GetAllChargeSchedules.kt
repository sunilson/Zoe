package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDao
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.scheduleCore.data.toEntity
import at.sunilson.scheduleCore.domain.entities.Schedule
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllChargeSchedules @Inject constructor(private val chargeScheduleDao: ChargeScheduleDao) :
    FlowUseCase<List<Schedule>, String>() {
    override fun run(params: String) =
        chargeScheduleDao.getAllChargeSchedulesForVehicle(params).map { it.map { it.toEntity() } }
}