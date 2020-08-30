package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkScheduleDay(val startTime: String, val duration: Int)