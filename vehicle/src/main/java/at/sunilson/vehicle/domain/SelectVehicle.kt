package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import javax.inject.Inject

internal class SelectVehicle @Inject constructor(private val vehicleRepository: VehicleRepository) :
    UseCase<Unit, String>() {
    override fun run(params: String) = Result.of<Unit, Exception> {
        vehicleRepository.selectedVehicle = params
    }
}