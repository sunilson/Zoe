package at.sunilson.hvacschedule.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.hvacschedule.data.HvacScheduleDatabase
import at.sunilson.scheduleCore.data.toEntity
import at.sunilson.scheduleCore.domain.entities.Schedule
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllHvacSchedules @Inject constructor(private val scheduleDatabase: HvacScheduleDatabase) :
    FlowUseCase<List<Schedule>, String>() {
    override fun run(params: String) =
        scheduleDatabase.hvacScheduleDao().getAllSchedulesForVehicle(params).map { it.map { it.toEntity() } }
}