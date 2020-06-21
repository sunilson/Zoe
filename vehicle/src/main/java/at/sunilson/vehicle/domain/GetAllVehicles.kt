package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.entities.Vehicle
import javax.inject.Inject

class GetAllVehicles @Inject constructor(private val vehicleRepository: VehicleRepository) :
    FlowUseCase<List<Vehicle>, Unit>() {
    override fun run(params: Unit) = vehicleRepository.getAllVehicles()
}