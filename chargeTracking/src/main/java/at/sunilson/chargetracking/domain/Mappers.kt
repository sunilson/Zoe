package at.sunilson.chargetracking.domain

import androidx.work.WorkInfo
import at.sunilson.chargetracking.domain.entities.ChargeTracker

fun WorkInfo.toChargeTracker(vin: String) = ChargeTracker(
    vin,
    when (state) {
        WorkInfo.State.ENQUEUED -> ChargeTracker.State.WAITING
        WorkInfo.State.RUNNING -> ChargeTracker.State.WORKING
        WorkInfo.State.SUCCEEDED -> ChargeTracker.State.COMPLETED
        WorkInfo.State.FAILED -> ChargeTracker.State.COMPLETED
        WorkInfo.State.BLOCKED -> ChargeTracker.State.BLOCKED
        WorkInfo.State.CANCELLED -> ChargeTracker.State.COMPLETED
    }
)