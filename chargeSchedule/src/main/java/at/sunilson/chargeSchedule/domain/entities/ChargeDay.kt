package at.sunilson.chargeSchedule.domain.entities

import java.time.LocalTime
internal data class ChargeDay(val dayOfWeek: WeekDay, val startTime: LocalTime, val duration: Int) {
    enum class WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}