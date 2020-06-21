package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.database.databaseentities.DatabaseVehicle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVehicle @Inject constructor(private val vehicleRepository: VehicleRepository) :
    FlowUseCase<DatabaseVehicle?, String>() {
    override fun run(params: String): Flow<DatabaseVehicle?> {
        TODO("Not yet implemented")
    }
}