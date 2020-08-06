package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.ChargeSettingsDay
import at.sunilson.chargeSchedule.data.models.ChargeSettingsSchedule
import at.sunilson.chargeSchedule.domain.entities.ChargeDay
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import java.time.ZonedDateTime

fun ChargeSettingsSchedule.toEntity() = ChargeSchedule(
    id,
    activated,
    listOfNotNull(
        monday?.toEntity(ChargeDay.WeekDay.MONDAY),
        tuesday?.toEntity(ChargeDay.WeekDay.TUESDAY),
        wednesday?.toEntity(ChargeDay.WeekDay.WEDNESDAY),
        thursday?.toEntity(ChargeDay.WeekDay.THURSDAY),
        friday?.toEntity(ChargeDay.WeekDay.FRIDAY),
        saturday?.toEntity(ChargeDay.WeekDay.SATURDAY),
        sunday?.toEntity(ChargeDay.WeekDay.SUNDAY)
    )
)

fun ChargeSettingsDay.toEntity(day: ChargeDay.WeekDay) =
    ChargeDay(day, ZonedDateTime.parse(startTime), duration)