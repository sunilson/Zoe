package at.sunilson.vehicle.presentation.hvacStartDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.vehiclecore.domain.GetHVACInstantPreferences
import at.sunilson.vehiclecore.domain.HVACPreferences
import at.sunilson.vehiclecore.domain.SaveHVACInstantPreferences
import at.sunilson.vehiclecore.domain.StartHVAC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

internal sealed class StartHVACEvent {
    object HVACStarted : StartHVACEvent()
    object HVACNotStarted : StartHVACEvent()
}

internal data class StartHVACState(val temperature: Float = 21f, val startTime: LocalTime? = null)

@HiltViewModel
internal class StartHVACViewModel @Inject constructor(
    private val saveHVACInstantPreferences: SaveHVACInstantPreferences,
    private val getHVACInstantPreferences: GetHVACInstantPreferences,
    private val startHVAC: StartHVAC
) : ViewModel(), ContainerHost<StartHVACState, StartHVACEvent> {

    override val container = container<StartHVACState, StartHVACEvent>(StartHVACState())

    init {
        intent {
            getHVACInstantPreferences(Unit).collect {
                reduce {
                    state.copy(
                        temperature = it.temperature.toFloat(),
                        startTime = it.time
                    )
                }
            }
        }

        viewModelScope.launch {
            container.stateFlow.collect { state ->
                saveHVACInstantPreferences(
                    HVACPreferences(
                        state.temperature.toInt(),
                        state.startTime
                    )
                )
            }
        }
    }

    fun startClicked() = intent {
        startHVAC(HVACPreferences(state.temperature.toInt(), state.startTime)).fold(
            { postSideEffect(StartHVACEvent.HVACStarted) },
            {
                Timber.e(it, "HVAC could not be started!")
                postSideEffect(StartHVACEvent.HVACNotStarted)
            }
        )
    }

    fun temperatureChanged(temperature: Float) = intent {
        reduce { state.copy(temperature = temperature) }
    }

    fun timeSet(localTime: LocalTime) = intent {
        if (localTime == LocalTime.now() || localTime.isBefore(LocalTime.now())) {
            reduce { state.copy(startTime = null) }
        } else {
            reduce { state.copy(startTime = localTime) }
        }
    }
}
