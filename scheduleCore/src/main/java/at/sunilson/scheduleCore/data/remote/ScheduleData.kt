package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleData(
    val id: String,
    val type: String,
    val attributes: NetworkScheduleAttributes
)
