package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.UseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.Result
import javax.inject.Inject

internal class SelectVehicle @Inject constructor(private val vehicleRepository: VehicleCoreRepository) :
    UseCase<Unit, String>() {
    override fun run(params: String) = Result.of<Unit, Exception> {
        vehicleRepository.selectedVehicle = params
    }
}