package at.arkulpa.notifications.data

import android.content.SharedPreferences
import androidx.core.content.edit
import at.arkulpa.notifications.domain.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    NotificationRepository {

    override fun chargeErrorNotificationEnabled(vin: String): Boolean {
        return sharedPreferences.getBoolean("chargeError$vin", false)
    }

    override fun toggleChargeErrorNotification(vin: String, value: Boolean) {
        sharedPreferences.edit { putBoolean("chargeError$vin", value) }
    }

    override fun chargedEightyPercentNotificationEnabled(vin: String): Boolean {
        return sharedPreferences.getBoolean("chargedEightyPercent$vin", false)
    }

    override fun toggleChargedEightyPercentNotification(vin: String, value: Boolean) {
        sharedPreferences.edit { putBoolean("chargedEightyPercent$vin", value) }
    }

    override fun chargeStartedNotificationEnabled(vin: String): Boolean {
        return sharedPreferences.getBoolean("chargeStarted$vin", false)
    }

    override fun toggleChargedStartedNotification(vin: String, value: Boolean) {
        sharedPreferences.edit { putBoolean("chargeStarted$vin", value) }
    }

    override fun chargeFinishedNotificationEnabled(vin: String): Boolean {
        return sharedPreferences.getBoolean("chargeFinished$vin", false)
    }

    override fun toggleChargedFinishedNotification(vin: String, value: Boolean) {
        sharedPreferences.edit { putBoolean("chargeFinished$vin", value) }
    }

    override fun lowBatteryNotificationEnabled(vin: String): Boolean {
        return sharedPreferences.getBoolean("lowBattery$vin", false)
    }

    override fun toggleLowBatteryNotification(vin: String, value: Boolean) {
        sharedPreferences.edit { putBoolean("lowBattery$vin", value) }
    }
}