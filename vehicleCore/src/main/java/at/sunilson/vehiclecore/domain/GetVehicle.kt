package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetVehicle @Inject constructor(private val vehicleDao: VehicleDao) :
    AsyncUseCase<Vehicle?, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Vehicle?, Exception> {
        vehicleDao.getVehicle(params).first()?.vehicle
    }
}