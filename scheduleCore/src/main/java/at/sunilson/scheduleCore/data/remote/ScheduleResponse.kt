package at.sunilson.scheduleCore.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleResponse(val data: ScheduleData)