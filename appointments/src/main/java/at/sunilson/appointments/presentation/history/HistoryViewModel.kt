package at.sunilson.appointments.presentation.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.GetAllServices
import at.sunilson.appointments.domain.RefreshServiceHistory
import at.sunilson.appointments.domain.entities.Service
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class HistoryState(
    val loading: Boolean = false,
    val services: List<Service> = listOf()
)

internal sealed class HistoryEvent

internal class HistoryViewModel @ViewModelInject constructor(
    private val refreshServiceHistory: RefreshServiceHistory,
    private val getAllServices: GetAllServices
) : UniDirectionalViewModel<HistoryState, HistoryEvent>(HistoryState()) {

    fun loadHistory(vin: String) {
        viewModelScope.launch {
            getAllServices(vin).collect {
                setState { copy(services = it) }
            }
        }
    }

    fun refreshHistory(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshServiceHistory(vin)
            setState { copy(loading = false) }
        }
    }
}