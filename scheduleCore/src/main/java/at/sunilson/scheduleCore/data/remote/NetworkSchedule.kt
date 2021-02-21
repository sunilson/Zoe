package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkSchedule(
    val id: Int,
    val activated: Boolean,
    val monday: NetworkScheduleDay?,
    val tuesday: NetworkScheduleDay?,
    val wednesday: NetworkScheduleDay?,
    val thursday: NetworkScheduleDay?,
    val friday: NetworkScheduleDay?,
    val saturday: NetworkScheduleDay?,
    val sunday: NetworkScheduleDay?
)
