package at.sunilson.hvacschedule.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.hvacschedule.data.HvacScheduleService
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.scheduleCore.data.createNetworkChargeSchedule
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal data class UpdateChargeScheduleParams(
    val vin: String,
    val chargeSchedules: List<Schedule>
)

internal class UpdateChargeSchedule @Inject constructor(
    private val chargeScheduleService: HvacScheduleService,
    private val refreshAllChargeSchedules: RefreshAllHvacSchedules,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, UpdateChargeScheduleParams>() {
    override suspend fun run(params: UpdateChargeScheduleParams) =
        SuspendableResult.of<Unit, Exception> {
            chargeScheduleService.setChargingSchedule(
                vehicleCoreRepository.kamereonAccountID,
                params.vin,
                KamereonPostBody(
                    KamereonPostBody.Data(
                        type = "HvacSchedule",
                        attributes = mapOf(
                            "schedules" to createNetworkChargeSchedule(params.chargeSchedules)
                        )
                    )
                )
            )
            refreshAllChargeSchedules(params.vin)
        }
}
