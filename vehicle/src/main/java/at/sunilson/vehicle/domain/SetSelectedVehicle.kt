package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.UseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.Result
import javax.inject.Inject

class SetSelectedVehicle @Inject constructor(private val vehicleCoreRepository: VehicleCoreRepository) :
    UseCase<Unit, String>() {
    override fun run(params: String) = Result.of<Unit, Exception> {
        vehicleCoreRepository.selectedVehicle = params
    }
}