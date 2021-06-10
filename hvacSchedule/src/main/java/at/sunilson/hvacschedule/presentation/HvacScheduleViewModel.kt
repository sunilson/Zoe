package at.sunilson.hvacschedule.presentation

import at.sunilson.hvacschedule.domain.GetAllHvacSchedules
import at.sunilson.hvacschedule.domain.RefreshAllHvacSchedules
import at.sunilson.hvacschedule.domain.SetChargeMode
import at.sunilson.hvacschedule.domain.SetChargeModeParams
import at.sunilson.hvacschedule.domain.UpdateChargeSchedule
import at.sunilson.hvacschedule.domain.UpdateChargeScheduleParams
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.scheduleCore.presentation.SchedulesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HvacScheduleViewModel @Inject constructor(
    private val refreshAllHvacSchedules: RefreshAllHvacSchedules,
    private val getAllHvacSchedules: GetAllHvacSchedules,
    private val setChargeMode: SetChargeMode,
    private val updateChargeSchedule: UpdateChargeSchedule
) : SchedulesViewModel() {

    override fun actuallyGetAllChargeSchedules(vin: String) = getAllHvacSchedules(vin)

    override suspend fun actuallyRefreshSchedules(vin: String) = refreshAllHvacSchedules(vin)

    override suspend fun actuallySaveSchedule(
        vin: String,
        schedules: List<Schedule>
    ) = updateChargeSchedule(UpdateChargeScheduleParams(vin, schedules))

    override suspend fun actuallySetScheduleType(
        vin: String,
        scheduleType: ScheduleType
    ) = setChargeMode(SetChargeModeParams(vin, scheduleType))
}
