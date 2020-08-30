package at.sunilson.scheduleCore.presentation

import androidx.lifecycle.viewModelScope
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

data class ScheduleState(
    val loading: Boolean = false,
    val settingChargeMode: Boolean = false,
    val chargeType: ScheduleType = ScheduleType.ALWAYS,
    val schedules: List<Schedule> = listOf(),
    val schedulesUpdated: Boolean = false
)

sealed class ScheduleEvent
object ModeNowAlways : ScheduleEvent()
object SettingModeFailed : ScheduleEvent()
object UpdatingSchedulesFailed : ScheduleEvent()
data class AskForSaveApproval(val exit: Boolean) : ScheduleEvent()
object Exit : ScheduleEvent()

abstract class SchedulesViewModel :
    UniDirectionalViewModel<ScheduleState, ScheduleEvent>(ScheduleState()) {

    private var chargeScheduleJob: Job? = null

    fun loadChargeSchedules(vin: String) {
        chargeScheduleJob?.cancel()
        chargeScheduleJob = viewModelScope.launch {
            actuallyGetAllChargeSchedules(vin).collect {
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
                actuallyRefreshSchedules(vin).fold(
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
                actuallySetScheduleType(
                    vin,
                    if (always) ScheduleType.ALWAYS else ScheduleType.SCHEDULED
                ).fold(
                    { if (always) sendEvent(ModeNowAlways) },
                    {
                        sendEvent(SettingModeFailed)
                        Timber.e(it, "Could not set charge mode!")
                        setState { copy(chargeType = if (always) ScheduleType.SCHEDULED else ScheduleType.ALWAYS) }
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
                actuallySaveSchedule(vin, state.schedules).fold(
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

    abstract fun actuallyGetAllChargeSchedules(vin: String): Flow<List<Schedule>>

    abstract suspend fun actuallyRefreshSchedules(vin: String): SuspendableResult<List<Schedule>, Exception>

    abstract suspend fun actuallySetScheduleType(
        vin: String,
        scheduleType: ScheduleType
    ): SuspendableResult<Unit, Exception>

    abstract suspend fun actuallySaveSchedule(
        vin: String,
        schedules: List<Schedule>
    ): SuspendableResult<Unit, Exception>
}