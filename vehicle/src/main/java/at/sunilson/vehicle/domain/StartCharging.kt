package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class StartCharging @Inject constructor(
    private val vehicleService: VehicleService,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, String>() {
    override suspend fun run(vin: String) = SuspendableResult.of<Unit, Exception> {
        vehicleService.startCharging(
            vehicleCoreRepository.kamereonAccountID,
            vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "ChargingStart",
                    mapOf("action" to "start")
                )
            )
        )
    }
}
