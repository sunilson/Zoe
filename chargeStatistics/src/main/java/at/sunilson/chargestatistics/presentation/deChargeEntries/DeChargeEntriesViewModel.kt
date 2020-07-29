package at.sunilson.chargestatistics.presentation.deChargeEntries

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.chargestatistics.domain.GetDeChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class DeChargeEntriesState(val deChargingProcedures: List<DeChargingProcedure> = listOf())
internal sealed class DeChargeEntriesEvent

internal class DeChargeEntriesViewModel @ViewModelInject constructor(private val getDeChargingProcedures: GetDeChargingProcedures) :
    UniDirectionalViewModel<DeChargeEntriesState, DeChargeEntriesEvent>(DeChargeEntriesState()) {

    fun loadChargeProcedures(vin: String) {
        viewModelScope.launch {
            getDeChargingProcedures(vin).collect {
                setState { copy(deChargingProcedures = it) }
            }
        }
    }
}