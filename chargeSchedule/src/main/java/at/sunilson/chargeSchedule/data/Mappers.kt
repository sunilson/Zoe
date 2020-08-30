package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsDay
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsSchedule
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import java.time.LocalTime

internal fun ChargeSettingsSchedule.toEntity(type: ScheduleType) = Schedule(
    id,
    type,
    activated,
    listOfNotNull(
        monday?.toEntity(ScheduleDay.WeekDay.MONDAY),
        tuesday?.toEntity(ScheduleDay.WeekDay.TUESDAY),
        wednesday?.toEntity(ScheduleDay.WeekDay.WEDNESDAY),
        thursday?.toEntity(ScheduleDay.WeekDay.THURSDAY),
        friday?.toEntity(ScheduleDay.WeekDay.FRIDAY),
        saturday?.toEntity(ScheduleDay.WeekDay.SATURDAY),
        sunday?.toEntity(ScheduleDay.WeekDay.SUNDAY)
    )
)

internal fun ChargeSettingsDay.toEntity(day: ScheduleDay.WeekDay) =
    ScheduleDay(
        day,
        LocalTime.parse(startTime.removePrefix("T").removeSuffix("Z")),
        duration
    )