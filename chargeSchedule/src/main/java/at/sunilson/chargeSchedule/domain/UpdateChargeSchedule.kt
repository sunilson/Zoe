package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.scheduleCore.data.remote.NetworkScheduleDay
import at.sunilson.scheduleCore.data.remote.NetworkSchedule
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.presentationcore.extensions.padZero
import at.sunilson.scheduleCore.data.createNetworkChargeSchedule
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
            refreshAllChargeSchedules(params.vin)
        }
}