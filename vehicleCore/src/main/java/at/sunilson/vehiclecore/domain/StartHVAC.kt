package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.vehiclecore.data.VehicleCoreService
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

class StartHVAC @Inject constructor(
    private val vehicleService: VehicleCoreService,
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