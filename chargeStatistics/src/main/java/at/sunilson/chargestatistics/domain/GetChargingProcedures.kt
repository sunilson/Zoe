package at.sunilson.chargestatistics.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import at.sunilson.chargestatistics.data.ChargingProceduresPagingSource
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.core.usecases.FlowUseCase
import javax.inject.Inject

internal class GetChargingProcedures @Inject constructor(
    private val chargingProceduresPagingSource: ChargingProceduresPagingSource
) : FlowUseCase<PagingData<ChargingProcedure>, String>() {
    override fun run(params: String) = Pager(PagingConfig(pageSize = 20)) {
        chargingProceduresPagingSource
    }.flow

    companion object {
        const val CHARGING_THRESHOLD = 1
    }
}