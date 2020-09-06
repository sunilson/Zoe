package at.sunilson.contracts.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.contracts.domain.GetAllContrats
import at.sunilson.contracts.domain.RefreshContracts
import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal data class ContractsState(
    val loading: Boolean = false,
    val contracts: List<Contract> = listOf()
)

internal sealed class ContractsEvents
internal object RequestFailed : ContractsEvents()

internal class ContractsViewModel @ViewModelInject constructor(
    private val getAllContrats: GetAllContrats,
    private val refreshContracts: RefreshContracts
) : UniDirectionalViewModel<ContractsState, ContractsEvents>(ContractsState()) {

    private var contractsJob: Job? = null

    fun refreshConracts(vin: String) {
        viewModelScope.launch {
            setState { copy(loading = true) }
            refreshContracts(vin).fold(
                {},
                {
                    sendEvent(RequestFailed)
                    Timber.e(it)
                }
            )
            setState { copy(loading = false) }
        }
    }

    fun loadContracts(vin: String) {
        contractsJob?.cancel()
        contractsJob = viewModelScope.launch {
            getAllContrats(vin).collect {
                setState { copy(contracts = it) }
            }
        }
    }
}