package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDao
import at.sunilson.chargeSchedule.data.toEntity
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllChargeSchedules @Inject constructor(private val chargeScheduleDao: ChargeScheduleDao) :
    FlowUseCase<List<ChargeSchedule>, String>() {
    override fun run(params: String) =
        chargeScheduleDao.getAllChargeSchedulesForVehicle(params).map { it.map { it.toEntity() } }
}