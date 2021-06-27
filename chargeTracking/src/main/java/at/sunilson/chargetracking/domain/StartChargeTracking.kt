package at.sunilson.chargetracking.domain

import android.annotation.SuppressLint
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import at.sunilson.chargetracking.workmanager.ChargeTrackingWorker
import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Starts a periodic request that checks the vehicles charging state and battery level and logs
 * the data for statistical analysis
 */
class StartChargeTracking @Inject constructor(private val workManager: WorkManager) :
    UseCase<Unit, String>() {

    /**
     * @param params The id (probably VIN) of the vehicle to track
     */
    @SuppressLint("UnsafeExperimentalUsageError")
    override fun run(params: String) = Result.of<Unit, Exception> {
        workManager.enqueueUniquePeriodicWork(
            params,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<ChargeTrackingWorker>(15L, TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 60L, TimeUnit.SECONDS)
                .setInputData(workDataOf("vehicleId" to params))
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .addTag(CHARGE_TRACKER_TAG)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        )
    }

    companion object {
        const val CHARGE_TRACKER_TAG = "chargeTrackers"
    }
}
