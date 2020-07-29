package at.sunilson.chargetracking.workmanager

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.sunilson.chargetracking.domain.StartChargeTracking
import at.sunilson.chargetracking.domain.TrackVehicleChargeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class ChargeTrackingWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted private val params: WorkerParameters,
    private val trackVehicleChargeState: TrackVehicleChargeState,
    private val startChargeTracking: StartChargeTracking
) : CoroutineWorker(context, params) {
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        val vehicleId = params.inputData.getString("vehicleId")!!

        Timber.d("Starting charge tracking")

        if(runAttemptCount > 5) return@withContext Result.failure()

        return@withContext trackVehicleChargeState(vehicleId).fold(
            {
                Timber.d("Charge tracking done")
                Result.success()
            },
            {
                Timber.e(it, "Charge tracking failed")
                Result.retry()
            }
        )
    }
}