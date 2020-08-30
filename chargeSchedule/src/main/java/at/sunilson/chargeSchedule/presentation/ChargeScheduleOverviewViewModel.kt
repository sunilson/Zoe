package at.sunilson.chargeSchedule.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.chargeSchedule.domain.GetAllChargeSchedules
import at.sunilson.chargeSchedule.domain.RefreshAllChargeSchedules
import at.sunilson.chargeSchedule.domain.SetChargeMode
import at.sunilson.chargeSchedule.domain.SetChargeModeParams
import at.sunilson.chargeSchedule.domain.UpdateChargeSchedule
import at.sunilson.chargeSchedule.domain.UpdateChargeScheduleParams
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class ChargeScheduleOverviewState(
    val loading: Boolean = false,
    val settingChargeMode: Boolean = false,
    val chargeType: ScheduleType = ScheduleType.ALWAYS,
    val schedules: List<Schedule> = listOf(),
    val schedulesUpdated: Boolean = false
)

internal sealed class ChargeScheduleOverviewEvent
internal object ChargeModeNowAlways : ChargeScheduleOverviewEvent()
internal object SettingChargeModeFailed : ChargeScheduleOverviewEvent()
internal object UpdatingSchedulesFailed : ChargeScheduleOverviewEvent()
internal data class AskForSaveApproval(val exit: Boolean) : ChargeScheduleOverviewEvent()
internal object Exit : ChargeScheduleOverviewEvent()

internal class ChargeScheduleOverviewViewModel @ViewModelInject constructor(
    private val getAllChargeSchedules: GetAllChargeSchedules,
    private val refreshAllChargeSchedules: RefreshAllChargeSchedules,
    private val setChargeMode: SetChargeMode,
    private val updateChargeSchedule: UpdateChargeSchedule
) : UniDirectionalViewModel<ChargeScheduleOverviewState, ChargeScheduleOverviewEvent>(
    ChargeScheduleOverviewState()
) {

    private var chargeScheduleJob: Job? = null

    fun loadChargeSchedules(vin: String) {
        chargeScheduleJob?.cancel()
        chargeScheduleJob = viewModelScope.launch {
            getAllChargeSchedules(vin).collect {
                setState {
                    copy(
                        chargeType = it.firstOrNull()?.scheduleType ?: ScheduleType.ALWAYS,
                        schedules = it
                    )
                }
            }
        }
    }

    fun refreshSchedules(vin: String) {
        getState { state ->
            if (state.schedulesUpdated) {
                sendEvent(AskForSaveApproval(false))
                return@getState
            }

            setState { copy(loading = true) }
            viewModelScope.launch {
                refreshAllChargeSchedules(vin).fold(
                    { Timber.d(it.toString()) },
                    { Timber.e(it) }
                )
                setState { copy(loading = false) }
            }
        }
    }

    fun toggleChargeMode(vin: String, always: Boolean) {
        getState { state ->

            if (state.schedulesUpdated) {
                sendEvent(AskForSaveApproval(false))
                return@getState
            }

            setState {
                copy(
                    settingChargeMode = true,
                    chargeType = if (always) ScheduleType.ALWAYS else ScheduleType.SCHEDULED
                )
            }

            viewModelScope.launch {
                setChargeMode(
                    SetChargeModeParams(
                        vin,
                        if (always) ScheduleType.ALWAYS else ScheduleType.SCHEDULED
                    )
                ).fold(
                    { if (always) sendEvent(ChargeModeNowAlways) },
                    {
                        sendEvent(SettingChargeModeFailed)
                        Timber.e(it, "Could not set charge mode!")
                    }
                )

                setState { copy(settingChargeMode = false) }
            }
        }
    }

    fun updateSchedules(chargeSchedule: Schedule) {
        getState { state ->
            if (state.loading) return@getState

            val scheduleIndex = state.schedules.indexOfFirst { it.id == chargeSchedule.id }
            if (scheduleIndex == -1) return@getState

            setState {
                copy(
                    schedulesUpdated = true,
                    schedules = schedules
                        .toMutableList()
                        .apply { set(scheduleIndex, chargeSchedule) }
                )
            }
        }
    }

    fun askForSaveApproval(exit: Boolean) {
        getState { state ->
            if (state.schedulesUpdated) {
                sendEvent(AskForSaveApproval(exit))
            } else {
                if (exit) {
                    sendEvent(Exit)
                }
            }
        }
    }

    fun deleteChangedSchedules(vin: String, exit: Boolean) {
        //TODO
        setState { copy(schedulesUpdated = false) }
        loadChargeSchedules(vin)
        if (exit) {
            sendEvent(Exit)
        }
    }

    fun saveSchedules(vin: String, exit: Boolean) {
        getState { state ->
            setState { copy(loading = true) }
            viewModelScope.launch {
                updateChargeSchedule(UpdateChargeScheduleParams(vin, state.schedules)).fold(
                    { setState { copy(schedulesUpdated = false) } },
                    {
                        sendEvent(UpdatingSchedulesFailed)
                        Timber.e(it, "Updating schedules failed!")
                    }
                )
                setState { copy(loading = false) }
                if (exit) {
                    sendEvent(Exit)
                }
            }
        }
    }
}