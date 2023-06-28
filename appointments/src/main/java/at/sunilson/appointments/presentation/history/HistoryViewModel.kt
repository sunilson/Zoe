package at.sunilson.appointments.presentation.history

import androidx.lifecycle.ViewModel
import at.sunilson.appointments.domain.GetAllServices
import at.sunilson.appointments.domain.RefreshServiceHistory
import at.sunilson.appointments.domain.entities.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

internal data class HistoryState(
    val loading: Boolean = false,
    val services: List<Service> = listOf()
)

internal sealed class HistorySideEffects

@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val refreshServiceHistory: RefreshServiceHistory,
    private val getAllServices: GetAllServices
) : ViewModel(), ContainerHost<HistoryState, HistorySideEffects> {

    override val container = container<HistoryState, HistorySideEffects>(HistoryState())

    private var servicesJob: Job? = null

    fun viewCreated(vin: String) {
        servicesJob?.cancel()
        servicesJob = intent {
            repeatOnSubscription { }
            getAllServices(vin).collect {
                reduce { state.copy(services = it) }
            }
        }
    }

    fun refreshHistory(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshServiceHistory(vin)
        reduce { state.copy(loading = false) }
    }
}
