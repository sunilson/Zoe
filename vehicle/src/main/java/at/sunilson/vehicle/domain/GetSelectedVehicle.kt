package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.data.toEntity
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetSelectedVehicle @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService,
    private val vehicleDao: VehicleDao
) : FlowUseCase<Vehicle?, Unit>() {
    override fun run(params: Unit) = flow {
        val selectedId = vehicleRepository.selectedVehicle
        if (selectedId == null) {
            val vehicle = vehicleDao.getAllVehicles().first().firstOrNull()
            if (vehicle != null) {
                vehicleRepository.selectedVehicle = vehicle.vin
                emitAll(vehicleDao.getVehicle(vehicle.vin))
            } else {
                emit(null)
            }
        } else {
            emitAll(vehicleDao.getVehicle(selectedId))
        }
    }.map { it?.toEntity() }
}