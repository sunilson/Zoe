package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class StopHVAC @Inject constructor(
    private val vehicleService: VehicleService,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<Unit, Exception> {
        val vin =
            vehicleCoreRepository.selectedVehicle.firstOrNull() ?: error("No vehicle selected")
        vehicleService.startHVAC(
            vehicleCoreRepository.kamereonAccountID,
            vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "HvacStart",
                    mapOf(
                        "action" to "cancel",
                    )
                )
            )
        )
    }
}