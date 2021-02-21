package at.sunilson.vehicle.presentation.utils

object TimeUtils {
    fun formatMinuteDuration(minutes: Int): String {
        val hours = minutes / 60
        val m = minutes - hours * 60
        return "${String.format("%02d", hours)}h:${String.format("%02d", m)}m"
    }
}
