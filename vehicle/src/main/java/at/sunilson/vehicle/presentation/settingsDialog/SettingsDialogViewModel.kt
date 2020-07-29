package at.sunilson.vehicle.presentation.settingsDialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehicle.domain.SetSelectedVehicle
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class SettingsDialogState(val vehicles: List<Vehicle> = listOf())
internal sealed class SettingsDialogEvent {
    object VehicleSelected : SettingsDialogEvent()
}

internal class SettingsDialogViewModel @ViewModelInject constructor(
    private val getAllVehicles: GetAllVehicles,
    private val setSelectedVehicle: SetSelectedVehicle
) : UniDirectionalViewModel<SettingsDialogState, SettingsDialogEvent>(SettingsDialogState()) {

    init {
        viewModelScope.launch {
            getAllVehicles(Unit).collect { setState { copy(vehicles = it) } }
        }
    }

    fun selectVehicle(vin: String) {
        setSelectedVehicle(vin).success { sendEvent(SettingsDialogEvent.VehicleSelected) }
    }
}