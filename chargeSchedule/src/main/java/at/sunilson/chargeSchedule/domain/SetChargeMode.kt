package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.chargeSchedule.domain.entities.ChargeType
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

data class SetChargeModeParams(val vin: String, val chargeType: ChargeType)

internal class SetChargeMode @Inject constructor(
    private val chargeScheduleService: ChargeScheduleService,
    private val refreshAllChargeSchedules: RefreshAllChargeSchedules,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, SetChargeModeParams>() {
    override suspend fun run(params: SetChargeModeParams) = SuspendableResult.of<Unit, Exception> {
        chargeScheduleService.setChargeMode(
            vehicleCoreRepository.kamereonAccountID,
            params.vin,
            KamereonPostBody(
                KamereonPostBody.Data(
                    "ChargeMode",
                    mapOf(
                        "action" to when (params.chargeType) {
                            ChargeType.ALWAYS -> "always_charging"
                            ChargeType.SCHEDULED -> "schedule_mode"
                        }
                    )
                )
            )
        )

        refreshAllChargeSchedules(params.vin)
    }
}