package at.arkulpa.notifications.domain.entities

data class SendNotificationParams(
    val title: String,
    val body: String,
    val highPriority: Boolean = false
)
