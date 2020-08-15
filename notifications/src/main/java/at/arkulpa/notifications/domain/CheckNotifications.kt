package at.arkulpa.notifications.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import at.arkulpa.notifications.domain.entities.CheckNotificationsParams
import at.arkulpa.notifications.domain.entities.SendNotificationParams
import at.sunilson.core.usecases.UseCase
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.failure
import com.squareup.moshi.Moshi
import timber.log.Timber
import javax.inject.Inject

class CheckNotifications @Inject constructor(
    private val repository: NotificationRepository,
    private val sharedPreferences: SharedPreferences,
    private val sendNotification: SendNotification
) : UseCase<Unit, CheckNotificationsParams>() {

    private val moshiAdapter = Moshi.Builder().build().adapter(Vehicle.BatteryStatus::class.java)

    override fun run(params: CheckNotificationsParams) = Result.of<Unit, Exception> {
        val oldStatus = try {
            moshiAdapter.fromJson(sharedPreferences.getString("batteryStatus${params.vin}", ""))
        } catch (e: Throwable) {
            null
        }

        checkChargeFinished(params.vin, params.batteryStatus, oldStatus)
        checkChargeStarted(params.vin, params.batteryStatus, oldStatus)
        checkLowBattery(params.vin, params.batteryStatus, oldStatus)
        checkChargedEightyPercent(params.vin, params.batteryStatus, oldStatus)

        sharedPreferences.edit {
            putString(
                "batteryStatus${params.vin}",
                moshiAdapter.toJson(params.batteryStatus)
            )
        }
    }

    private fun checkChargedEightyPercent(
        vin: String,
        newStatus: Vehicle.BatteryStatus,
        oldStatus: Vehicle.BatteryStatus?
    ) {
        if (repository.chargedEightyPercentNotificationEnabled(vin) && newStatus.batteryLevel >= 80 && oldStatus?.batteryLevel ?: 100 < 80) {
            sendNotification(
                SendNotificationParams(
                    "80% geladen",
                    "Die Battere ist nun zu ${newStatus.batteryLevel}% geladen!"
                )
            ).failure { Timber.e(it, "Sending notification failed") }
        }
    }

    private fun checkLowBattery(
        vin: String,
        newStatus: Vehicle.BatteryStatus,
        oldStatus: Vehicle.BatteryStatus?
    ) {
        if (repository.lowBatteryNotificationEnabled(vin) && newStatus.batteryLevel < 20 && oldStatus?.batteryLevel ?: 100 >= 20) {
            sendNotification(
                SendNotificationParams(
                    "Batterie niedrig",
                    "Die Batterie hat noch weniger als 20% Ladung übrig"
                )
            ).failure { Timber.e(it, "Sending notification failed") }
        }
    }

    private fun checkChargeStarted(
        vin: String,
        newStatus: Vehicle.BatteryStatus,
        oldStatus: Vehicle.BatteryStatus?
    ) {
        if (repository.chargeFinishedNotificationEnabled(vin) && oldStatus?.isCharging != true && newStatus.isCharging && oldStatus?.pluggedIn == true) {
            sendNotification(
                SendNotificationParams(
                    "Laden gestartet",
                    "Der Ladevorgang wurde gestartet!"
                )
            ).failure { Timber.e(it, "Sending notification failed") }
        }
    }

    private fun checkChargeFinished(
        vin: String,
        newStatus: Vehicle.BatteryStatus,
        oldStatus: Vehicle.BatteryStatus?
    ) {
        if (repository.chargeFinishedNotificationEnabled(vin) && oldStatus?.isCharging == true && newStatus.chargeState == Vehicle.BatteryStatus.ChargeState.CHARGE_ENDED) {
            sendNotification(
                SendNotificationParams(
                    "Laden fertig",
                    "Der Ladevorgang ist abgeschlossen!"
                )
            ).failure { Timber.e(it, "Sending notification failed") }
        }
    }
}