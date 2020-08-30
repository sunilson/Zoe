package at.sunilson.scheduleCore.domain.entities

data class Schedule(
    val id: Int,
    val scheduleType: ScheduleType,
    val activated: Boolean,
    val days: List<ScheduleDay>
)