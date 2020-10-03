package at.sunilson.chargetracking.domain

import androidx.work.WorkInfo
import androidx.work.WorkManager
import at.sunilson.chargetracking.domain.entities.isTracking
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.google.common.util.concurrent.ListenableFuture
import javax.inject.Inject


class CheckIfTrackerIsRunning @Inject constructor(private val workManager: WorkManager) :
    AsyncUseCase<Boolean, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Boolean, Exception> {
        val statuses: ListenableFuture<List<WorkInfo>> =
            workManager.getWorkInfosForUniqueWork(params)
        try {
            val workInfoList = statuses.get()
            val workInfo = workInfoList.firstOrNull()
            workInfo?.toChargeTracker(params)?.isTracking == true
        } catch (e: Exception) {
            false
        }
    }
}