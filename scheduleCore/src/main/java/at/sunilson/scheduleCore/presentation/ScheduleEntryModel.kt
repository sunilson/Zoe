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
import at.sunilson.scheduleCore.domain.entities.ChargeScheduleDay
import at.sunilson.scheduleCore.domain.entities.HvacScheduleDay
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

    @EpoxyAttribute
    @JvmField
    var isHvac: Boolean = false

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun bind(holder: Holder) = holder.run {
        name.text = "Programm ${schedule.id}"

        //If there is a day set we can setup sliders, time texts, etc., otherwise the schedule is not active
        val firstDay = schedule.days.firstOrNull()

        setupSlider(firstDay)
        setupTexts(firstDay)
        setupToggle()
        setUpDayList(schedule, slider)
    }

    private fun Holder.setupToggle() {
        toggle.isChecked = schedule.activated
        toggle.setOnClickListener {
            scheduleUpdated(schedule.copy(activated = toggle.isChecked))
        }
    }

    private fun Holder.setupTexts(day: ScheduleDay?) {
        endTime.isVisible = day is ChargeScheduleDay
        endTimeTitle.isVisible = day is ChargeScheduleDay

        if (day != null) {
            startTime.text = day.time.format(formatter)
            startTime.setOnClickListener { setTime(it.context) }
            if (day is ChargeScheduleDay) {
                endTime.text = day.time.plusMinutes(day.duration.toLong()).format(formatter)
            }
        } else {
            startTime.text = "-"
            endTime.text = "-"
        }

        if (!isHvac) {
            startTimeTitle.text = startTime.context.getString(R.string.start)
        } else {
            startTimeTitle.text = startTime.context.getString(R.string.ready_at)
        }
    }

    private fun Holder.setupSlider(day: ScheduleDay?) {
        slider.clearOnSliderTouchListeners()

        slider.setLabelFormatter {
            val hours = (it / 60).toInt()
            val minutes = (it - hours * 60).toInt()
            "${hours.padZero()}:${minutes.padZero()}"
        }

        when (day) {
            is ChargeScheduleDay -> {
                slider.isVisible = true
                slider.value = day.duration.toFloat()
                slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: Slider) {}
                    override fun onStopTrackingTouch(slider: Slider) {
                        updateEndTime(slider.value.toInt())
                    }
                })
                slider.addOnChangeListener { _, value, _ ->
                    endTime.text = day.time.plusMinutes(value.toLong()).format(formatter)
                }
            }
            null,
            is HvacScheduleDay -> slider.isVisible = false
        }
    }

    /**
     * Opens a [TimePickerDialog] that updates the current schedule on success
     */
    private fun setTime(context: Context) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val (adjustedMinute, adjustedHour) = clampMinuteAndHour(minute to hourOfDay)
                val adjustedTime = LocalTime.now().withHour(adjustedHour).withMinute(adjustedMinute)
                scheduleUpdated(schedule.copy(days = schedule.days.map {
                    when (it) {
                        is ChargeScheduleDay -> it.copy(time = adjustedTime)
                        is HvacScheduleDay -> it.copy(time = adjustedTime)
                    }
                }))
            },
            12,
            0,
            true
        ).show()
    }

    private fun clampMinuteAndHour(minuteAndHour: Pair<Int, Int>): Pair<Int, Int> {
        var adjustedHour = minuteAndHour.second
        val adjustedMinute = when (minuteAndHour.first) {
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

        return adjustedMinute to adjustedHour
    }

    private fun updateEndTime(duration: Int) {
        scheduleUpdated(schedule.copy(days = schedule.days.map {
            when (it) {
                is ChargeScheduleDay -> it.copy(duration = duration)
                is HvacScheduleDay -> error("Cant update end time of hvac schedule")
            }
        }))
    }

    private fun Holder.getChipForDay(dayOfWeek: ScheduleDay.WeekDay) = container.findViewById<Chip>(
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

    /**
     * Populate list of week days and set click listeners
     */
    private fun Holder.setUpDayList(chargeSchedule: Schedule, durationSlider: Slider) {
        container.children.filterIsInstance<Chip>().forEach { it.isChecked = false }

        ScheduleDay.WeekDay.values().forEach { dayOfWeek ->
            val view = getChipForDay(dayOfWeek)
            view.isChecked = chargeSchedule.days.any { it.dayOfWeek == dayOfWeek }
            setChipClickListener(view, chargeSchedule, dayOfWeek)
        }
    }

    private fun Holder.setChipClickListener(
        chip: Chip,
        chargeSchedule: Schedule,
        dayOfWeek: ScheduleDay.WeekDay
    ) {
        chip.setOnClickListener {
            val index = chargeSchedule.days.indexOfFirst { it.dayOfWeek == dayOfWeek }

            val updatedChargeSchedule = if (index == -1) {
                //If we already have a day we use those values to populate the new day
                val alreadyExistingDay = chargeSchedule.days.firstOrNull()
                val newChargeDay = if (isHvac) {
                    HvacScheduleDay(
                        dayOfWeek,
                        (alreadyExistingDay as? HvacScheduleDay)?.time ?: defaultStartTime
                    )
                } else {
                    ChargeScheduleDay(
                        dayOfWeek,
                        (alreadyExistingDay as? ChargeScheduleDay)?.time ?: defaultStartTime,
                        (alreadyExistingDay as? ChargeScheduleDay)?.duration ?: 60
                    )
                }

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

    private val defaultStartTime: LocalTime
        get() = LocalTime.now().withHour(12).withMinute(0)

    class Holder : KotlinEpoxyHolder() {
        val toggle by bind<SwitchMaterial>(R.id.schedule_activated)
        val slider by bind<Slider>(R.id.duration_slider)
        val startTime by bind<TextView>(R.id.schedule_start_time)
        val endTime by bind<TextView>(R.id.schedule_end_time)
        val startTimeTitle by bind<TextView>(R.id.schedule_start_time_title)
        val endTimeTitle by bind<TextView>(R.id.schedule_end_time_title)
        val name by bind<TextView>(R.id.schedule_name)
        val container by bind<ConstraintLayout>(R.id.container)
    }
}
