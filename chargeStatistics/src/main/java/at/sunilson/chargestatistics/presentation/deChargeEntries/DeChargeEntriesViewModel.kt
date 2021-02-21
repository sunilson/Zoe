package at.sunilson.chargestatistics.presentation.deChargeEntries

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import at.sunilson.chargestatistics.domain.GetDeChargingProcedures
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

internal data class DeChargeEntriesState(
    val deChargingProcedures: PagingData<DeChargingProcedure> = PagingData.empty()
)

internal sealed class DeChargeEntriesSideEffects

internal class DeChargeEntriesViewModel @ViewModelInject constructor(
    private val getDeChargingProcedures: GetDeChargingProcedures
) : ViewModel(),
    ContainerHost<DeChargeEntriesState, DeChargeEntriesSideEffects> {

    override val container = container<DeChargeEntriesState, DeChargeEntriesSideEffects>(
        DeChargeEntriesState()
    )

    fun viewCreated(vin: String) = intent {
        getDeChargingProcedures(vin).collect {
            reduce { state.copy(deChargingProcedures = it) }
        }
    }
}
