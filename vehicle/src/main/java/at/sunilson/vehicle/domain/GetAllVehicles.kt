package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllVehicles @Inject constructor(private val vehicleDao: VehicleDao) :
    FlowUseCase<List<Vehicle>, Unit>() {
    override fun run(params: Unit) = vehicleDao.getAllVehicles().map { it.map { it.toEntity() } }
}