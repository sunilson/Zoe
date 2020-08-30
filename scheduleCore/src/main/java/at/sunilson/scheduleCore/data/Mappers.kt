package at.sunilson.scheduleCore.data

import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import java.time.LocalTime
import java.time.temporal.ChronoField

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