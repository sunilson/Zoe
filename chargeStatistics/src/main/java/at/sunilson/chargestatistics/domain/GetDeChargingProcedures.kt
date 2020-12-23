package at.sunilson.chargestatistics.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import at.sunilson.chargestatistics.data.DeChargingProceduresPagingSource
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.core.usecases.FlowUseCase
import javax.inject.Inject

internal class GetDeChargingProcedures @Inject constructor(
    private val deChargingProceduresPagingSource: DeChargingProceduresPagingSource
) : FlowUseCase<PagingData<DeChargingProcedure>, String>() {

    override fun run(params: String) = Pager(PagingConfig(pageSize = 20)) {
        deChargingProceduresPagingSource
    }.flow
}