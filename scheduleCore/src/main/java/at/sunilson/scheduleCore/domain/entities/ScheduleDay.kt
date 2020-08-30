package at.sunilson.scheduleCore.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.time.LocalTime

@JsonClass(generateAdapter = true)
data class ScheduleDay(val dayOfWeek: WeekDay, val startTime: LocalTime, val duration: Int) {
    @Keep
    enum class WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}