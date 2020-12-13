package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject


internal class StartHVAC @Inject constructor(
    private val vehicleService: VehicleService,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, HVACPreferences>() {
    override suspend fun run(params: HVACPreferences) = SuspendableResult.of<Unit, Exception> {
        val vin =
            vehicleCoreRepository.selectedVehicle.firstOrNull() ?: error("No vehicle selected")

        var dateTimeString = ""
        if (params.time != null) {
            dateTimeString = OffsetDateTime
                .now()
                .atZoneSameInstant(ZoneId.systemDefault())
                .withHour(params.time.hour)
                .withMinute(params.time.minute)
                .formatPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        }

        vehicleService.startHVAC(
            vehicleCoreRepository.kamereonAccountID,
            vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "HvacStart",
                    mapOf(
                        "action" to "start",
                        "targetTemperature" to params.temperature,
                        "startDateTime" to dateTimeString
                    )
                )
            )
        )
    }
}