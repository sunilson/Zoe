package at.arkulpa.notifications.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.arkulpa.notifications.R
import at.arkulpa.notifications.domain.entities.SendNotificationParams
import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SendNotification @Inject constructor(@ApplicationContext private val context: Context) :
    UseCase<Unit, SendNotificationParams>() {

    private val notificationManager: NotificationManagerCompat
        get() = NotificationManagerCompat.from(context)

    override fun run(params: SendNotificationParams) = Result.of<Unit, Exception> {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(params.title)
            .setContentText(params.body)
            .setSmallIcon(R.drawable.notification_icon)
            .setColor(context.getColor(R.color.blue))
            .setPriority(
                if (params.highPriority) {
                    NotificationCompat.PRIORITY_MAX
                } else {
                    NotificationCompat.PRIORITY_DEFAULT
                }
            )
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.default_notification_channel_title)
            val descriptionText =
                context.getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(
                    DEFAULT_NOTIFICATION_CHANNEL_ID,
                    name,
                    importance
                ).apply {
                    description = descriptionText
                }

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "reminders"
        const val LOW_BATTERY_NOTIFICATION = 1
        const val CHARGE_DONE_NOTIFICATION = 2
    }
}
