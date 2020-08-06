package at.sunilson.chargeSchedule.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.chargeSchedule.domain.GetAllChargeSchedules
import at.sunilson.chargeSchedule.domain.RefreshAllChargeSchedules
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ChargeScheduleOverviewState
sealed class ChargeScheduleOverviewEvent

internal class ChargeScheduleOverviewViewModel @ViewModelInject constructor(
    private val getAllChargeSchedules: GetAllChargeSchedules,
    private val refreshAllChargeSchedules: RefreshAllChargeSchedules
) : UniDirectionalViewModel<ChargeScheduleOverviewState, ChargeScheduleOverviewEvent>(
    ChargeScheduleOverviewState()
) {
    fun refreshSchedules(vin: String) {
        viewModelScope.launch {
            refreshAllChargeSchedules(vin).fold(
                {
                    Timber.d(it.toString())
                },
                { Timber.e(it) }
            )
        }
    }
}