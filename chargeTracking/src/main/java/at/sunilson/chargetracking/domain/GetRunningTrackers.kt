package at.sunilson.chargetracking.domain

import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkManager
import at.sunilson.chargetracking.domain.StartChargeTracking.Companion.CHARGE_TRACKER_TAG
import at.sunilson.chargetracking.domain.entities.ChargeTracker
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRunningTrackers @Inject constructor(private val workManager: WorkManager) :
    AsyncUseCase<List<ChargeTracker>, Unit>() {
    override suspend fun run(params: Unit) = SuspendableResult.of<List<ChargeTracker>, Exception> {
        workManager
            .getWorkInfosByTagLiveData(CHARGE_TRACKER_TAG)
            .asFlow()
            .first()
            .map { workInfo ->
                ChargeTracker(
                    workInfo.id.toString(),
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> ChargeTracker.State.WAITING
                        WorkInfo.State.RUNNING -> ChargeTracker.State.WORKING
                        WorkInfo.State.SUCCEEDED -> ChargeTracker.State.WAITING
                        WorkInfo.State.FAILED -> ChargeTracker.State.ERROR
                        WorkInfo.State.BLOCKED -> ChargeTracker.State.WAITING
                        WorkInfo.State.CANCELLED -> ChargeTracker.State.ERROR
                    }
                )
            }
    }
}