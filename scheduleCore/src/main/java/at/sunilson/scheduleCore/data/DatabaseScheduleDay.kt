package at.sunilson.scheduleCore.data

import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DatabaseScheduleDay(
    val dayOfWeek: ScheduleDay.WeekDay,
    val startTime: Int,
    val duration: Int
)