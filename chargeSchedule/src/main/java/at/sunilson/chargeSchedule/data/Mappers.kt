package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsDay
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsSchedule
import at.sunilson.chargeSchedule.domain.entities.ChargeDay
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import java.time.LocalTime

internal fun ChargeSettingsSchedule.toEntity() = ChargeSchedule(
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

internal fun ChargeSettingsDay.toEntity(day: ChargeDay.WeekDay) =
    ChargeDay(
        day,
        LocalTime.parse(startTime.removePrefix("T").removeSuffix("Z")),
        duration
    )