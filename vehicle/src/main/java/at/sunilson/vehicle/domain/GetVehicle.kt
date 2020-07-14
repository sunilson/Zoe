package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.database.VehicleDao
import at.sunilson.database.mappers.toEntity
import at.sunilson.entities.Vehicle
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetVehicle @Inject constructor(private val vehicleDao: VehicleDao) :
    FlowUseCase<Vehicle?, String>() {
    override fun run(params: String) = vehicleDao.getVehicle(params).map { it?.toEntity() }
}