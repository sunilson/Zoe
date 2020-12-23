package at.sunilson.chargestatistics.presentation.chargeEntries

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import at.sunilson.chargestatistics.domain.GetChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class EntriesState(val chargingProcedures: PagingData<ChargingProcedure> = PagingData.empty())
internal sealed class EntriesEvent

internal class ChargeEntriesViewModel @ViewModelInject constructor(private val getChargingProcedures: GetChargingProcedures) :
    UniDirectionalViewModel<EntriesState, EntriesEvent>(EntriesState()) {

    fun loadChargeProcedures(vin: String) {
        viewModelScope.launch {
            getChargingProcedures(vin).collect {
                setState { copy(chargingProcedures = it) }
            }
        }
    }
}