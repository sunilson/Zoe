package at.sunilson.vehicle.presentation.settingsDialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class SettingsDialogState(val vehicles: List<Vehicle> = listOf())
internal sealed class SettingsDialogEvent

internal class SettingsDialogViewModel @ViewModelInject constructor(private val getAllVehicles: GetAllVehicles) :
    UniDirectionalViewModel<SettingsDialogState, SettingsDialogEvent>(SettingsDialogState()) {

    init {
        viewModelScope.launch {
            getAllVehicles(Unit).collect { setState { copy(vehicles = it) } }
        }
    }
}