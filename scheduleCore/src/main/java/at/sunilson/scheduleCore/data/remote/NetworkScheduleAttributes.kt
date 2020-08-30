package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkScheduleAttributes(val mode: String, val schedules: List<NetworkSchedule>)