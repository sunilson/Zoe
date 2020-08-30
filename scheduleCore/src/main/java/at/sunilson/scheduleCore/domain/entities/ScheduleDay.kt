package at.sunilson.scheduleCore.domain.entities

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import dev.zacsweers.moshisealed.annotations.DefaultNull
import dev.zacsweers.moshisealed.annotations.TypeLabel
import java.time.LocalTime

@JsonClass(generateAdapter = true, generator = "sealed:type")
@DefaultNull
sealed class ScheduleDay {
    abstract val dayOfWeek: WeekDay
    abstract val time: LocalTime

    @Keep
    enum class WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}

@TypeLabel("charge")
@JsonClass(generateAdapter = true)
data class ChargeScheduleDay(
    override val dayOfWeek: WeekDay,
    override val time: LocalTime,
    val duration: Int
) : ScheduleDay()

@TypeLabel("hvac")
@JsonClass(generateAdapter = true)
data class HvacScheduleDay(override val dayOfWeek: WeekDay, override val time: LocalTime) : ScheduleDay()