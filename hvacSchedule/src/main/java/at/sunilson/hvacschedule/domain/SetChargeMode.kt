package at.sunilson.hvacschedule.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.hvacschedule.data.HvacScheduleService
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

data class SetChargeModeParams(val vin: String, val chargeType: ScheduleType)

internal class SetChargeMode @Inject constructor(
    private val chargeScheduleService: HvacScheduleService,
    private val refreshAllChargeSchedules: RefreshAllHvacSchedules,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, SetChargeModeParams>() {
    override suspend fun run(params: SetChargeModeParams) = SuspendableResult.of<Unit, Exception> {
        chargeScheduleService.setChargeMode(
            vehicleCoreRepository.kamereonAccountID,
            params.vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "HvacMode",
                    mapOf(
                        "action" to when (params.chargeType) {
                            ScheduleType.ALWAYS -> "always_charging"
                            ScheduleType.SCHEDULED -> "schedule_mode"
                        }
                    )
                )
            )
        )

        refreshAllChargeSchedules(params.vin)
    }
}