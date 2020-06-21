package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import timber.log.Timber
import javax.inject.Inject

class RefreshAllVehicles @Inject constructor(private val vehicleRepository: VehicleRepository) :
    AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        val vehicles = vehicleRepository.getRefreshedVehicles().get()
        Timber.d("Got vehicle list: $vehicles")

        Timber.d("Refreshing vehicles battery status...")
        //TODO Parallel
        val vehiclesWithBattery = vehicles.map {
            val batteryStatus = vehicleRepository.getBatteryStatus(it.vin).get()
            it.copy(batteryStatus = batteryStatus)
        }

        //TODO Filter only for zoes
        vehicleRepository.saveVehiclesToLocalStorage(vehiclesWithBattery).get()
    }
}