package at.sunilson.chargetracking.domain

import androidx.lifecycle.asFlow
import androidx.work.WorkManager
import at.sunilson.chargetracking.domain.entities.ChargeTracker
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetRunningChargeTrackers @Inject constructor(private val workManager: WorkManager) :
    FlowUseCase<List<ChargeTracker>, List<String>>() {

    override fun run(params: List<String>): Flow<List<ChargeTracker>> {
        val flows = params.mapNotNull { vin ->
            try {
                workManager
                    .getWorkInfosForUniqueWorkLiveData(vin)
                    .asFlow()
                    .map { workInfos ->
                        val workInfo = workInfos.firstOrNull() ?: return@map null
                        workInfo.toChargeTracker(vin)
                    }
                    .filterNotNull()
            } catch (e: Exception) {
                null
            }
        }

        return channelFlow {
            val results = Array<ChargeTracker?>(flows.size) { null }
            send(results.filterNotNull())
            flows.forEachIndexed { index, flow ->
                launch {
                    flow.collect {
                        results[index] = it
                        send(results.filterNotNull())
                    }
                }
            }
        }
    }
}
