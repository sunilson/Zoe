package at.sunilson.appointments.presentation.history

import androidx.lifecycle.ViewModel
import at.sunilson.appointments.domain.GetAllServices
import at.sunilson.appointments.domain.RefreshServiceHistory
import at.sunilson.appointments.domain.entities.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.coroutines.transformFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.strict.orbit
import org.orbitmvi.orbit.syntax.strict.reduce
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

    fun viewCreated(vin: String) {
        orbit {
            transformFlow { getAllServices(vin) }.reduce { state.copy(services = event) }
        }
    }

    fun refreshHistory(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshServiceHistory(vin)
        reduce { state.copy(loading = false) }
    }
}
