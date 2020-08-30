package at.sunilson.scheduleCore.presentation

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.padZero
import at.sunilson.scheduleCore.R
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleDay
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@EpoxyModelClass
abstract class ScheduleEntryModel :
    EpoxyModelWithHolder<ScheduleEntryModel.Holder>() {

    override fun getDefaultLayout() = R.layout.schedule_entry

    @EpoxyAttribute
    lateinit var schedule: Schedule

    @EpoxyAttribute
    lateinit var scheduleUpdated: (Schedule) -> Unit

    @EpoxyAttribute
    lateinit var scheduleToggled: (Boolean) -> Unit

    override fun bind(holder: Holder) = holder.run {
        name.text = "Ladeprogramm ${schedule.id}"

        //If there is a day set we can setup sliders, time texts, etc., otherwise the schedule is not active
        val firstDay = schedule.days.firstOrNull()
        if (firstDay != null) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            startTime.text = firstDay.startTime.format(formatter)
            startTime.setOnClickListener { setTime(it.context) }

            endTime.text =
                firstDay.startTime.plusMinutes(firstDay.duration.toLong()).format(formatter)

            //Only setup slider when a day is already set
            slider.value = firstDay.duration.toFloat()
            slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}
                override fun onStopTrackingTouch(slider: Slider) {
                    updateEndTime(slider.value.toInt())
                }
            })
            slider.addOnChangeListener { _, value, _ ->
                endTime.text = firstDay.startTime.plusMinutes(value.toLong()).format(formatter)
            }
            slider.isVisible = true
        } else {
            startTime.text = "-"
            endTime.text = "-"
            slider.isVisible = false
        }

        slider.setLabelFormatter {
            val hours = (it / 60).toInt()
            val minutes = (it - hours * 60).toInt()
            "${hours.padZero()}:${minutes.padZero()}"
        }

        toggle.isChecked = schedule.activated
        toggle.setOnClickListener {
            scheduleUpdated(schedule.copy(activated = toggle.isChecked))
        }

        setUpDayList(schedule, slider)
    }

    /**
     * Opens a [TimePickerDialog] that updates the current schedule on success
     */
    private fun setTime(context: Context) {
        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { p0, hourOfDay, minute ->
                var adjustedHour = hourOfDay
                val adjustedMinute = when (minute) {
                    in (0..7) -> 0
                    in (8..22) -> 15
                    in (23..37) -> 30
                    in (38..52) -> 45
                    else -> {
                        if (adjustedHour < 23) {
                            adjustedHour++
                            0
                        } else {
                            59
                        }
                    }
                }
                val adjustedTime = LocalTime.now().withHour(adjustedHour).withMinute(adjustedMinute)
                scheduleUpdated(
                    schedule.copy
                        (days = schedule.days.map { it.copy(startTime = adjustedTime) })
                )
            },
            12,
            0,
            true
        ).show()
    }

    private fun updateEndTime(duration: Int) {
        scheduleUpdated(schedule.copy(days = schedule.days.map {
            it.copy(duration = duration)
        }))
    }

    /**
     * Populate list of week days and set click listeners
     */
    private fun Holder.setUpDayList(chargeSchedule:  Schedule, durationSlider: Slider) {
        container.children.filterIsInstance<Chip>().forEach { it.isChecked = false }

        ScheduleDay.WeekDay.values().forEach { dayOfWeek ->
            val view = container.findViewById<Chip>(
                when (dayOfWeek) {
                    ScheduleDay.WeekDay.MONDAY -> R.id.chip_monday
                    ScheduleDay.WeekDay.TUESDAY -> R.id.chip_tuesday
                    ScheduleDay.WeekDay.WEDNESDAY -> R.id.chip_wednesday
                    ScheduleDay.WeekDay.THURSDAY -> R.id.chip_thursday
                    ScheduleDay.WeekDay.FRIDAY -> R.id.chip_friday
                    ScheduleDay.WeekDay.SATURDAY -> R.id.chip_saturday
                    ScheduleDay.WeekDay.SUNDAY -> R.id.chip_sunday
                }
            )

            view.isChecked = chargeSchedule.days.any { it.dayOfWeek == dayOfWeek }

            view.setOnClickListener {
                val index = chargeSchedule.days.indexOfFirst { it.dayOfWeek == dayOfWeek }

                val updatedChargeSchedule = if (index == -1) {
                    //If we already have a day we use those values to populate the new day
                    val alreadyExistingDay = chargeSchedule.days.firstOrNull()
                    val newChargeDay = ScheduleDay(
                        dayOfWeek,
                        alreadyExistingDay?.startTime ?: defaultStartTime,
                        alreadyExistingDay?.duration ?: slider.value.toInt()
                    )

                    chargeSchedule.copy(days = chargeSchedule
                        .days
                        .toMutableList()
                        .apply { add(newChargeDay) }
                    )
                } else {
                    chargeSchedule.copy(
                        days = chargeSchedule
                            .days
                            .toMutableList()
                            .apply { removeAt(index) }
                    )
                }

                scheduleUpdated(updatedChargeSchedule)
            }
        }
    }

    private val defaultStartTime: LocalTime
        get() = LocalTime.now().withHour(12).withMinute(0)

    class Holder : KotlinEpoxyHolder() {
        val toggle by bind<SwitchMaterial>(R.id.schedule_activated)
        val slider by bind<Slider>(R.id.duration_slider)
        val startTime by bind<TextView>(R.id.schedule_start_time)
        val endTime by bind<TextView>(R.id.schedule_end_time)
        val name by bind<TextView>(R.id.schedule_name)
        val container by bind<ConstraintLayout>(R.id.container)
    }
}
