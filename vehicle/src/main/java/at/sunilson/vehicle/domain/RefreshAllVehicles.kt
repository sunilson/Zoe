package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import timber.log.Timber
import javax.inject.Inject

class RefreshAllVehicles @Inject constructor(private val vehicleRepository: VehicleRepository) :
    AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        val vehicles = vehicleRepository.getRefreshedVehicles().get()
        Timber.i("Got vehicle list: $vehicles")
        //TODO Filter only for zoes
        vehicleRepository.saveVehiclesToLocalStorage(vehicles).get()
    }
}