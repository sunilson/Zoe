package at.sunilson.chargeSchedule.domain

import at.sunilson.chargeSchedule.data.ChargeScheduleDao
import javax.inject.Inject

internal class GetAllChargeSchedules @Inject constructor(private val chargeScheduleDao: ChargeScheduleDao) {
}