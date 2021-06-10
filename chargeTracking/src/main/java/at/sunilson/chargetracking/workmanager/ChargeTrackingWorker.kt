package at.sunilson.chargetracking.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.arkulpa.notifications.domain.CheckNotifications
import at.arkulpa.notifications.domain.entities.CheckNotificationsParams
import at.sunilson.chargetracking.domain.TrackVehicleChargeState
import com.github.kittinunf.result.failure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class ChargeTrackingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val params: WorkerParameters,
    private val trackVehicleChargeState: TrackVehicleChargeState,
    private val checkNotifications: CheckNotifications
) : CoroutineWorker(context, params) {
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        val vehicleId = params.inputData.getString("vehicleId")!!

        Timber.d("Starting charge tracking")

        if (runAttemptCount > 3) {
            Timber.d("Reached run attempt count. Stopping for now")
            return@withContext Result.success()
        }

        return@withContext trackVehicleChargeState(vehicleId).fold(
            {
                Timber.d("Charge tracking done")
                checkNotifications(CheckNotificationsParams(vehicleId, it.batteryStatus)).failure {
                    Timber.e(it, "Could not send notifications")
                }
                Result.success()
            },
            {
                Timber.e(it, "Charge tracking failed")
                Result.retry()
            }
        )
    }
}
