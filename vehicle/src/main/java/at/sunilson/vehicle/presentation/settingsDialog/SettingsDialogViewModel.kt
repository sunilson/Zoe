package at.sunilson.vehicle.presentation.settingsDialog

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import at.sunilson.authentication.domain.LogoutUseCase
import at.sunilson.vehicle.domain.SelectVehicle
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.success
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

internal data class SettingsDialogState(val vehicles: List<Vehicle> = listOf())
internal sealed class SettingsDialogEvent {
    object VehicleSelected : SettingsDialogEvent()
    object LoggedOut : SettingsDialogEvent()
}

@HiltViewModel
internal class SettingsDialogViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getAllVehicles: GetAllVehicles,
    private val setSelectedVehicle: SelectVehicle,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel(), ContainerHost<SettingsDialogState, SettingsDialogEvent> {

    override val container =
        container<SettingsDialogState, SettingsDialogEvent>(SettingsDialogState())

    init {
        intent {
            getAllVehicles(Unit).collect {
                reduce { state.copy(vehicles = it) }
            }
        }
    }

    fun vehicleSelected(vin: String) = intent {
        setSelectedVehicle(vin).success { postSideEffect(SettingsDialogEvent.VehicleSelected) }
    }

    fun logoutClicked() = intent {
        logoutUseCase(Unit).fold(
            { postSideEffect(SettingsDialogEvent.LoggedOut) },
            {}
        )
    }

    fun themeChosen(theme: Int) {
        sharedPreferences.edit { putInt("theme", theme) }
    }
}
