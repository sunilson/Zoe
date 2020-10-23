package at.sunilson.appointments.presentation.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.RefreshServiceHistory
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.launch

internal data class HistoryState(val loading: Boolean = false)
internal sealed class HistoryEvent

internal class HistoryViewModel @ViewModelInject constructor(private val refreshServiceHistory: RefreshServiceHistory) :
    UniDirectionalViewModel<HistoryState, HistoryEvent>(HistoryState()) {

    fun loadHistory(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshServiceHistory(vin)
            setState { copy(loading = false) }
        }
    }

}