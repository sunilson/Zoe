package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeDay
import at.sunilson.chargeSchedule.data.models.local.DatabaseChargeSchedule
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsDay
import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsSchedule
import at.sunilson.chargeSchedule.domain.entities.ChargeDay
import at.sunilson.chargeSchedule.domain.entities.ChargeSchedule
import at.sunilson.chargeSchedule.domain.entities.ChargeType
import java.time.LocalTime
import java.time.temporal.ChronoField

internal fun ChargeSettingsSchedule.toEntity(type: ChargeType) = ChargeSchedule(
    id,
    type,
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

internal fun ChargeSchedule.toDatabaseEntity(vin: String) =
    DatabaseChargeSchedule(id, vin, chargeType, activated, days.map { it.toDatabaseEntity() })

internal fun ChargeDay.toDatabaseEntity() =
    DatabaseChargeDay(dayOfWeek, startTime.toSecondOfDay(), duration)

internal fun DatabaseChargeDay.toEntity() =
    ChargeDay(
        dayOfWeek,
        LocalTime.now().with(ChronoField.SECOND_OF_DAY, startTime.toLong()),
        duration
    )

internal fun DatabaseChargeSchedule.toEntity() =
    ChargeSchedule(id, chargeType, activated, days.map { it.toEntity() })