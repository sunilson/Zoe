package at.sunilson.contracts.domain

import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNearestExpiringContract @Inject constructor(private val getSelectedVehicle: GetSelectedVehicle) :
    FlowUseCase<Contract?, Unit>() {

    @Inject
    internal lateinit var getAllContrats: GetAllContrats

    override fun run(params: Unit) = getSelectedVehicle(Unit).flatMapLatest { vehicle ->
        if (vehicle == null) {
            flowOf()
        } else {
            getAllContrats(vehicle.vin).map { contracts -> contracts.firstOrNull() }
        }
    }
}
