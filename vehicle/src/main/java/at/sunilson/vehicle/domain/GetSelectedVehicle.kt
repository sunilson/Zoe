package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.entities.Vehicle
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSelectedVehicle @Inject constructor(private val vehicleRepository: VehicleRepository) :
    FlowUseCase<Vehicle?, Unit>() {
    override fun run(params: Unit) = flow {
        val selectedId = vehicleRepository.selectedVehicle
        if (selectedId == null) {
            val vehicle = vehicleRepository.getAllVehicles().first().firstOrNull()
            if (vehicle != null) {
                vehicleRepository.selectedVehicle = vehicle.vin
                emitAll(vehicleRepository.getVehicle(vehicle.vin))
            } else {
                emit(null)
            }
        } else {
            emitAll(vehicleRepository.getVehicle(selectedId))
        }
    }
}