package at.sunilson.chargetracking.workmanager

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.sunilson.chargetracking.domain.TrackVehicleChargeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ChargeTrackingWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: TrackVehicleChargeState
) : CoroutineWorker(context, params) {
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        useCase("Unit").fold(
            {},
            {}
        )

        Result.retry()
    }
}