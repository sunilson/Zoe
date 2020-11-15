package at.sunilson.vehicle.presentation.settingsDialog

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.authentication.domain.LogoutUseCase
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicle.domain.SelectVehicle
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class SettingsDialogState(val vehicles: List<Vehicle> = listOf())
internal sealed class SettingsDialogEvent {
    object VehicleSelected : SettingsDialogEvent()
    object LoggedOut : SettingsDialogEvent()

}

internal class SettingsDialogViewModel @ViewModelInject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getAllVehicles: GetAllVehicles,
    private val setSelectedVehicle: SelectVehicle,
    private val logoutUseCase: LogoutUseCase,
) : UniDirectionalViewModel<SettingsDialogState, SettingsDialogEvent>(SettingsDialogState()) {

    init {
        viewModelScope.launch {
            getAllVehicles(Unit).collect { setState { copy(vehicles = it) } }
        }
    }

    fun selectVehicle(vin: String) {
        viewModelScope.launch {
            setSelectedVehicle(vin).success { sendEvent(SettingsDialogEvent.VehicleSelected) }
        }
    }

    fun logout() {
        logoutUseCase(Unit).fold(
            { sendEvent(SettingsDialogEvent.LoggedOut) },
            {}
        )
    }

    fun themeChosen(theme: Int) {
        sharedPreferences.edit { putInt("theme", theme) }
    }
}