package at.sunilson.chargeSchedule.domain.entities

import java.time.ZonedDateTime

data class ChargeDay(val dayOfWeek: WeekDay, val startTime: ZonedDateTime, val duration: Int) {
    enum class WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}