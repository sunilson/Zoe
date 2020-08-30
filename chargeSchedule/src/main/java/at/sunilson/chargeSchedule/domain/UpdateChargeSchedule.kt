package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsDay
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsSchedule
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.KamereonPostBody
import at.sunilson.presentationcore.extensions.padZero
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
    private val type =
        Types.newParameterizedType(List::class.java, ChargeSettingsSchedule::class.java)
    private val moshiAdapter = Moshi.Builder().build().adapter<List<ChargeSettingsSchedule>>(type)

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

    private fun createNetworkChargeSchedule(schedules: List<Schedule>) = schedules.map {
        ChargeSettingsSchedule(
            it.id,
            it.activated,
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.MONDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.TUESDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.WEDNESDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.THURSDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.FRIDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.SATURDAY }?.toNetworkEntity(),
            it.days.firstOrNull { it.dayOfWeek == ScheduleDay.WeekDay.SUNDAY }?.toNetworkEntity()
        )
    }

    private fun ScheduleDay.toNetworkEntity() =
        ChargeSettingsDay("T${startTime.hour.padZero()}:${startTime.minute.padZero()}Z", duration)
}