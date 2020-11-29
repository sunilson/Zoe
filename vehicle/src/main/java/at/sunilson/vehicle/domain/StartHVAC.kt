package at.sunilson.vehicle.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
            dateTimeString = DateTimeFormatter
                .ISO_DATE_TIME
                .format(
                    ZonedDateTime
                        .from(params.time)
                        .withZoneSameInstant(ZoneId.systemDefault())
                )
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