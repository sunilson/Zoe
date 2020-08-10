package at.sunilson.chargeSchedule.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.time.LocalTime

@JsonClass(generateAdapter = true)
internal data class ChargeDay(val dayOfWeek: WeekDay, val startTime: LocalTime, val duration: Int) {
    @Keep
    enum class WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}