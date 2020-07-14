package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.data.entities.KamereonPostBody
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class StartClimateControl @Inject constructor(
    private val vehicleService: VehicleService,
    private val repository: VehicleRepository
) : AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        val selectedVehicle = requireNotNull(repository.selectedVehicle)
        vehicleService.startHVAC(
            repository.kamereonAccountID,
            selectedVehicle,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "HvacStart",
                    mapOf(
                        "action" to "start",
                        "targetTemperature" to 21
                    )
                )
            )
        )
    }
}