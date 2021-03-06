package at.sunilson.chargestatistics.presentation.chargeEntries

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import at.sunilson.chargestatistics.domain.GetChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

internal data class EntriesState(
    val chargingProcedures: PagingData<ChargingProcedure> = PagingData.empty()
)

internal sealed class EntriesSideEffects

@HiltViewModel
internal class ChargeEntriesViewModel @Inject constructor(
    private val getChargingProcedures: GetChargingProcedures
) : ViewModel(), ContainerHost<EntriesState, EntriesSideEffects> {

    override val container = container<EntriesState, EntriesSideEffects>(EntriesState())

    fun viewCreated(vin: String) = intent {
        getChargingProcedures(vin).collect {
            reduce { state.copy(chargingProcedures = it) }
        }
    }
}
