package at.arkulpa.notifications.presentation

import android.widget.Button
import android.widget.Switch
import at.arkulpa.notifications.R
import at.arkulpa.notifications.domain.NotificationRepository
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class NotificationWidgetModel : EpoxyModelWithHolder<NotificationWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.notification_widget

    @EpoxyAttribute
    lateinit var notificationRepository: NotificationRepository

    @EpoxyAttribute
    lateinit var chargeTrackButtonClicked: (String) -> Unit

    @EpoxyAttribute
    lateinit var vin: String

    override fun bind(holder: Holder) = holder.run {
        chargeTrackButton.setOnClickListener { chargeTrackButtonClicked(vin) }

        chargestartSwitch.isChecked = notificationRepository.chargeStartedNotificationEnabled(vin)
        chargeEndSwitch.isChecked = notificationRepository.chargeFinishedNotificationEnabled(vin)
        batteryLowSwitch.isChecked = notificationRepository.lowBatteryNotificationEnabled(vin)

        chargestartSwitch.setOnCheckedChangeListener { _, checked ->
            notificationRepository.toggleChargedStartedNotification(vin, checked)
        }

        chargeEndSwitch.setOnCheckedChangeListener { _, checked ->
            notificationRepository.toggleChargedFinishedNotification(vin, checked)
        }

        batteryLowSwitch.setOnCheckedChangeListener { _, checked ->
            notificationRepository.toggleLowBatteryNotification(vin, checked)
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val chargeTrackButton by bind<Button>(R.id.charge_tracking_button)
        val chargestartSwitch by bind<Switch>(R.id.charge_start)
        val chargeEndSwitch by bind<Switch>(R.id.charge_end)
        val batteryLowSwitch by bind<Switch>(R.id.battery_low)
    }
}
