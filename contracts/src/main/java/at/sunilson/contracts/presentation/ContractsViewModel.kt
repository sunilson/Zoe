package at.sunilson.contracts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.contracts.domain.GetAllContrats
import at.sunilson.contracts.domain.RefreshContracts
import at.sunilson.contracts.domain.entities.Contract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

internal data class ContractsState(
    val loading: Boolean = false,
    val contracts: List<Contract> = listOf()
)

internal sealed class ContractsSideEffects {
    internal object RequestFailed : ContractsSideEffects()
}

@HiltViewModel
internal class ContractsViewModel @Inject constructor(
    private val getAllContrats: GetAllContrats,
    private val refreshContracts: RefreshContracts
) : ViewModel(), ContainerHost<ContractsState, ContractsSideEffects> {

    private var contractsJob: Job? = null
    override val container = container<ContractsState, ContractsSideEffects>(ContractsState())

    fun refreshConractsRequested(vin: String) = intent {
        reduce { state.copy(loading = true) }
        refreshContracts(vin).fold(
            {},
            {
                postSideEffect(ContractsSideEffects.RequestFailed)
                Timber.e(it)
            }
        )
        reduce { state.copy(loading = false) }
    }

    fun viewCreated(vin: String) {
        contractsJob?.cancel()
        contractsJob = viewModelScope.launch {
            getAllContrats(vin).collect {
                intent { reduce { state.copy(contracts = it) } }
            }
        }
    }
}
