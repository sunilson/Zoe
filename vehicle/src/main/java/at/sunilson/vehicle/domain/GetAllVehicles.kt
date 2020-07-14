package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.database.VehicleDao
import at.sunilson.database.mappers.toEntity
import at.sunilson.entities.Vehicle
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllVehicles @Inject constructor(private val vehicleDao: VehicleDao) :
    FlowUseCase<List<Vehicle>, Unit>() {
    override fun run(params: Unit) = vehicleDao.getAllVehicles().map { it.map { it.toEntity() } }
}