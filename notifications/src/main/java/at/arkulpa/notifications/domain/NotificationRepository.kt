package at.arkulpa.notifications.domain

interface NotificationRepository {
    fun toggleChargeErrorNotification(vin: String, value: Boolean)
    fun chargeErrorNotificationEnabled(vin: String): Boolean

    fun toggleChargedStartedNotification(vin: String, value: Boolean)
    fun chargeStartedNotificationEnabled(vin: String): Boolean

    fun toggleChargedFinishedNotification(vin: String, value: Boolean)
    fun chargeFinishedNotificationEnabled(vin: String): Boolean

    fun toggleChargedEightyPercentNotification(vin: String, value: Boolean)
    fun chargedEightyPercentNotificationEnabled(vin: String): Boolean

    fun toggleLowBatteryNotification(vin: String, value: Boolean)
    fun lowBatteryNotificationEnabled(vin: String): Boolean
}