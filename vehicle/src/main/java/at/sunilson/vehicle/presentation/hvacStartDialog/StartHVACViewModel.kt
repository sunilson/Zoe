package at.sunilson.vehicle.presentation.hvacStartDialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.unidirectionalviewmodel.extensions.registerPureMiddleWare
import at.sunilson.vehicle.domain.GetHVACInstantPreferences
import at.sunilson.vehicle.domain.HVACPreferences
import at.sunilson.vehicle.domain.SaveHVACInstantPreferences
import at.sunilson.vehicle.domain.StartHVAC
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime

internal sealed class StartHVACEvent {
    object HVACStarted: StartHVACEvent()
    object HVACNotStarted: StartHVACEvent()
}
internal data class StartHVACState(val temperature: Float = 21f, val startTime: LocalTime? = null)

internal class StartHVACViewModel @ViewModelInject constructor(
    private val saveHVACInstantPreferences: SaveHVACInstantPreferences,
    private val getHVACInstantPreferences: GetHVACInstantPreferences,
    private val startHVAC: StartHVAC
) : UniDirectionalViewModel<StartHVACState, StartHVACEvent>(StartHVACState()) {

    init {
        viewModelScope.launch {
            getHVACInstantPreferences(Unit).collect {
                setState { copy(temperature = it.temperature.toFloat(), startTime = it.time) }
            }
        }

        registerPureMiddleWare { state ->
            viewModelScope.launch {
                saveHVACInstantPreferences(
                    HVACPreferences(
                        state.temperature.toInt(),
                        state.startTime
                    )
                )
            }
        }
    }

    fun startClicked() {
        getState { state ->
            viewModelScope.launch {
                startHVAC(HVACPreferences(state.temperature.toInt(), state.startTime)).fold(
                    { sendEvent(StartHVACEvent.HVACStarted) },
                    { sendEvent(StartHVACEvent.HVACNotStarted) }
                )
            }
        }
    }

    fun setTemperature(temperature: Float) {
        setState { copy(temperature = temperature) }
    }

    fun setTime(localTime: LocalTime) {
        if (localTime == LocalTime.now() || localTime.isBefore(LocalTime.now())) {
            setState { copy(startTime = null) }
        } else {
            setState { copy(startTime = localTime) }
        }
    }
}