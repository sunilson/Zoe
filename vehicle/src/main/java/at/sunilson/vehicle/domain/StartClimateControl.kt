package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class StartClimateControl @Inject constructor(private val repository: VehicleRepository) :
    AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        val selectedVehicle = requireNotNull(repository.selectedVehicle)
        repository.startClimeateControl(selectedVehicle).get()
    }
}