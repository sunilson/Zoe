package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.scheduleCore.data.createNetworkChargeSchedule
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.delay
import javax.inject.Inject

internal data class UpdateChargeScheduleParams(
    val vin: String,
    val chargeSchedules: List<Schedule>
)

internal class UpdateChargeSchedule @Inject constructor(
    private val chargeScheduleService: ChargeScheduleService,
    private val refreshAllChargeSchedules: RefreshAllChargeSchedules,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, UpdateChargeScheduleParams>() {
    override suspend fun run(params: UpdateChargeScheduleParams) =
        SuspendableResult.of<Unit, Exception> {
            // As a hacky workaround to the wonky Renault API we try to set and refresh multiple times
            setChargingSchedule(params)
            setChargingSchedule(params)
            refreshAllChargeSchedules(params.vin)
            delay(1000L)
            refreshAllChargeSchedules(params.vin)
        }

    private suspend fun setChargingSchedule(params: UpdateChargeScheduleParams) {
        chargeScheduleService.setChargingSchedule(
            vehicleCoreRepository.kamereonAccountID,
            params.vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    type = "ChargeSchedule",
                    attributes = mapOf(
                        "schedules" to createNetworkChargeSchedule(params.chargeSchedules)
                    )
                )
            )
        )
    }
}
