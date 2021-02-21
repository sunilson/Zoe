package at.sunilson.scheduleCore.data

import at.sunilson.presentationcore.extensions.padZero
import at.sunilson.scheduleCore.data.remote.NetworkSchedule
import at.sunilson.scheduleCore.data.remote.NetworkScheduleDay
import at.sunilson.scheduleCore.domain.entities.ChargeScheduleDay
import at.sunilson.scheduleCore.domain.entities.HvacScheduleDay
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import java.time.LocalTime

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

fun NetworkScheduleDay.toEntity(day: ScheduleDay.WeekDay): ScheduleDay {
    val dateString = startTime ?: readyAtTime ?: error("No time defined!")

    return if (startTime != null && duration != null) {
        ChargeScheduleDay(
            day,
            LocalTime.parse(startTime.removePrefix("T").removeSuffix("Z")),
            duration
        )
    } else if (readyAtTime != null) {
        HvacScheduleDay(
            day,
            LocalTime.parse(readyAtTime.removePrefix("T").removeSuffix("Z"))
        )
    } else {
        error("")
    }
}

fun Schedule.toDatabaseEntity(vin: String) =
    DatabaseSchedule(id, vin, scheduleType, activated, days)

fun DatabaseSchedule.toEntity() = Schedule(id, chargeType, activated, days)

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

fun ScheduleDay.toNetworkEntity() = when (this) {
    is ChargeScheduleDay -> NetworkScheduleDay(
        "T${time.hour.padZero()}:${time.minute.padZero()}Z",
        duration = duration
    )
    is HvacScheduleDay -> NetworkScheduleDay(readyAtTime = "T${time.hour.padZero()}:${time.minute.padZero()}Z")
}
