package at.sunilson.chargeSchedule.presentation

import at.sunilson.chargeSchedule.domain.GetAllChargeSchedules
import at.sunilson.chargeSchedule.domain.RefreshAllChargeSchedules
import at.sunilson.chargeSchedule.domain.SetChargeMode
import at.sunilson.chargeSchedule.domain.SetChargeModeParams
import at.sunilson.chargeSchedule.domain.UpdateChargeSchedule
import at.sunilson.chargeSchedule.domain.UpdateChargeScheduleParams
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.scheduleCore.presentation.SchedulesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ChargeScheduleOverviewViewModel @Inject constructor(
    private val getAllChargeSchedules: GetAllChargeSchedules,
    private val refreshAllChargeSchedules: RefreshAllChargeSchedules,
    private val setChargeMode: SetChargeMode,
    private val updateChargeSchedule: UpdateChargeSchedule
) : SchedulesViewModel() {

    override fun actuallyGetAllChargeSchedules(vin: String) = getAllChargeSchedules(vin)

    override suspend fun actuallyRefreshSchedules(vin: String) = refreshAllChargeSchedules(vin)

    override suspend fun actuallySetScheduleType(
        vin: String,
        scheduleType: ScheduleType
    ) = setChargeMode(SetChargeModeParams(vin, scheduleType))

    override suspend fun actuallySaveSchedule(
        vin: String,
        schedules: List<Schedule>
    ) = updateChargeSchedule(UpdateChargeScheduleParams(vin, schedules))
}
