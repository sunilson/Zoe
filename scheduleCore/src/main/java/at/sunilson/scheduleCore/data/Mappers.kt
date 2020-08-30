package at.sunilson.scheduleCore.data

import at.sunilson.presentationcore.extensions.padZero
import at.sunilson.scheduleCore.data.remote.NetworkSchedule
import at.sunilson.scheduleCore.data.remote.NetworkScheduleDay
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import java.time.LocalTime
import java.time.temporal.ChronoField

fun NetworkSchedule.toEntity(type: ScheduleType) = Schedule(
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

fun NetworkScheduleDay.toEntity(day: ScheduleDay.WeekDay) =
    ScheduleDay(
        day,
        LocalTime.parse(startTime.removePrefix("T").removeSuffix("Z")),
        duration
    )

fun Schedule.toDatabaseEntity(vin: String) =
    DatabaseSchedule(id, vin, scheduleType, activated, days.map { it.toDatabaseEntity() })

fun ScheduleDay.toDatabaseEntity() =
    DatabaseScheduleDay(dayOfWeek, startTime.toSecondOfDay(), duration)

fun DatabaseScheduleDay.toEntity() =
    ScheduleDay(
        dayOfWeek,
        LocalTime.now().with(ChronoField.SECOND_OF_DAY, startTime.toLong()),
        duration
    )

fun DatabaseSchedule.toEntity() =
    Schedule(id, chargeType, activated, days.map { it.toEntity() })

fun createNetworkChargeSchedule(schedules: List<Schedule>) = schedules.map {
    NetworkSchedule(
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

fun ScheduleDay.toNetworkEntity() =
    NetworkScheduleDay("T${startTime.hour.padZero()}:${startTime.minute.padZero()}Z", duration)