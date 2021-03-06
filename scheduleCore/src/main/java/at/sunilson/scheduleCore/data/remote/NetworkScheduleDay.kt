package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkScheduleDay(
    val startTime: String? = null,
    val readyAtTime: String? = null,
    val duration: Int? = null
)
