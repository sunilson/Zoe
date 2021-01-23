package at.sunilson.scheduleCore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

data class ScheduleState(
    val loading: Boolean = false,
    val settingChargeMode: Boolean = false,
    val chargeType: ScheduleType = ScheduleType.ALWAYS,
    val schedules: List<Schedule> = listOf(),
    val schedulesUpdated: Boolean = false
)

sealed class ScheduleSideEffects {
    object ModeNowAlways : ScheduleSideEffects()
    object SettingModeFailed : ScheduleSideEffects()
    object UpdatingSchedulesFailed : ScheduleSideEffects()
    data class AskForSaveApproval(val exit: Boolean) : ScheduleSideEffects()
    object Exit : ScheduleSideEffects()
}


abstract class SchedulesViewModel : ViewModel(), ContainerHost<ScheduleState, ScheduleSideEffects> {

    override val container = container<ScheduleState, ScheduleSideEffects>(ScheduleState())

    private var chargeScheduleJob: Job? = null

    fun viewCreated(vin: String) {
        chargeScheduleJob?.cancel()
        chargeScheduleJob = viewModelScope.launch {
            actuallyGetAllChargeSchedules(vin).collect {
                intent {
                    reduce {
                        state.copy(
                            chargeType = it.firstOrNull()?.scheduleType ?: ScheduleType.ALWAYS,
                            schedules = it
                        )
                    }
                }
            }
        }
    }

    fun refreshSchedules(vin: String) = intent {
        if (state.schedulesUpdated) {
            postSideEffect(ScheduleSideEffects.AskForSaveApproval(false))
            return@intent
        }

        reduce { state.copy(loading = true) }
        actuallyRefreshSchedules(vin).fold(
            { Timber.d(it.toString()) },
            { Timber.e(it) }
        )
        reduce { state.copy(loading = false) }
    }

    fun toggleChargeMode(vin: String, always: Boolean) = intent {
        if (state.schedulesUpdated) {
            postSideEffect(ScheduleSideEffects.AskForSaveApproval(false))
            return@intent
        }

        reduce {
            state.copy(
                settingChargeMode = true,
                chargeType = if (always) ScheduleType.ALWAYS else ScheduleType.SCHEDULED
            )
        }

        actuallySetScheduleType(
            vin,
            if (always) ScheduleType.ALWAYS else ScheduleType.SCHEDULED
        ).fold(
            { if (always) postSideEffect(ScheduleSideEffects.ModeNowAlways) },
            {
                postSideEffect(ScheduleSideEffects.SettingModeFailed)
                Timber.e(it, "Could not set charge mode!")
                reduce { state.copy(chargeType = if (always) ScheduleType.SCHEDULED else ScheduleType.ALWAYS) }
            }
        )

        reduce { state.copy(settingChargeMode = false) }
    }

    fun updateSchedules(chargeSchedule: Schedule) = intent {
        if (state.loading) return@intent

        val scheduleIndex = state.schedules.indexOfFirst { it.id == chargeSchedule.id }
        if (scheduleIndex == -1) return@intent

        reduce {
            state.copy(
                schedulesUpdated = true,
                schedules = state.schedules
                    .toMutableList()
                    .apply { set(scheduleIndex, chargeSchedule) }
            )
        }
    }

    fun askForSaveApproval(exit: Boolean) = intent {
        if (state.schedulesUpdated) {
            postSideEffect(ScheduleSideEffects.AskForSaveApproval(exit))
        } else {
            if (exit) {
                postSideEffect(ScheduleSideEffects.Exit)
            }
        }
    }

    fun deleteChangedSchedules(vin: String, exit: Boolean) = intent {
        reduce { state.copy(schedulesUpdated = false) }
        viewCreated(vin)
        if (exit) {
            postSideEffect(ScheduleSideEffects.Exit)
        }
    }

    fun saveSchedules(vin: String, exit: Boolean) = intent {
        reduce { state.copy(loading = true) }
        actuallySaveSchedule(vin, state.schedules).fold(
            { reduce { state.copy(schedulesUpdated = false) } },
            {
                postSideEffect(ScheduleSideEffects.UpdatingSchedulesFailed)
                Timber.e(it, "Updating schedules failed!")
            }
        )
        reduce { state.copy(loading = false) }
        if (exit) {
            postSideEffect(ScheduleSideEffects.Exit)
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