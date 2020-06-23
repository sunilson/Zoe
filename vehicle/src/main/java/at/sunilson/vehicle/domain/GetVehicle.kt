package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.entities.Vehicle
import javax.inject.Inject

internal class GetVehicle @Inject constructor(private val vehicleRepository: VehicleRepository) :
    FlowUseCase<Vehicle?, String>() {
    override fun run(params: String) = vehicleRepository.getVehicle(params)
}