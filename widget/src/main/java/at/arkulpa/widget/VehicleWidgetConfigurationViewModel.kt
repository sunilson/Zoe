package at.arkulpa.widget

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.arkulpa.widget.domain.SelectVehicleForWidget
import at.arkulpa.widget.domain.SelectVehicleForWidgetParams
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

data class VehicleWidgetState(val vehicles: List<Vehicle> = listOf())
sealed class VehicleWidgetSideEffects

internal class VehicleWidgetConfigurationViewModel @ViewModelInject constructor(
    private val getAllVehicles: GetAllVehicles,
    private val selectVehicleForWidget: SelectVehicleForWidget
) : ViewModel(), ContainerHost<VehicleWidgetState, VehicleWidgetSideEffects> {

    override val container = container<VehicleWidgetState, VehicleWidgetSideEffects>(VehicleWidgetState())

    init {
        viewModelScope.launch {
            getAllVehicles(Unit).collect {
                intent { reduce { state.copy(vehicles = it) } }
            }
        }
    }

    fun vehicleSelected(vehicle: Vehicle, widgetId: Int) {
        selectVehicleForWidget(SelectVehicleForWidgetParams(vehicle.vin, widgetId.toString()))
    }
}