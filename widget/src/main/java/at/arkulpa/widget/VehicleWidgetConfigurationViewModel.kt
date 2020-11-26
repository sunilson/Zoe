package at.arkulpa.widget

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.arkulpa.widget.domain.SelectVehicleForWidget
import at.arkulpa.widget.domain.SelectVehicleForWidgetParams
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class VehicleWidgetState(val vehicles: List<Vehicle> = listOf())
sealed class VehicleWidgetEvent

internal class VehicleWidgetConfigurationViewModel @ViewModelInject constructor(
    private val getAllVehicles: GetAllVehicles,
    private val selectVehicleForWidget: SelectVehicleForWidget
) : UniDirectionalViewModel<VehicleWidgetState, VehicleWidgetEvent>(VehicleWidgetState()) {

    init {
        viewModelScope.launch {
            getAllVehicles(Unit).collect {
                setState { copy(vehicles = it) }
            }
        }
    }

    fun vehicleSelected(vehicle: Vehicle, widgetId: Int) {
        selectVehicleForWidget(SelectVehicleForWidgetParams(vehicle.vin, widgetId.toString()))
    }
}