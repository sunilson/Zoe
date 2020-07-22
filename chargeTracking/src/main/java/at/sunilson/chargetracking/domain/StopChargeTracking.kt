package at.sunilson.chargetracking.domain

import androidx.lifecycle.asFlow
import androidx.work.Operation
import androidx.work.WorkManager
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StopChargeTracking @Inject constructor(private val workManager: WorkManager) : AsyncUseCase<Unit, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val operation = workManager.cancelUniqueWork(params)
        val result = operation.state.asFlow().first()
        if (result !is Operation.State.SUCCESS) {
            error("Could not cancel running tracker!")
        }
    }
}