package at.sunilson.chargestatistics.data

import androidx.paging.PagingSource
import at.sunilson.chargestatistics.domain.ExtractChargingProcedures
import at.sunilson.chargestatistics.domain.ExtractDeChargingProcedures
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.chargetracking.domain.GetOffsetChargeTrackingPoints
import at.sunilson.chargetracking.domain.GetOffsetChargeTrackingPointsParams
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class DeChargingProceduresPagingSource @Inject constructor(
    private val getOffsetChargeTrackingPoints: GetOffsetChargeTrackingPoints,
    private val extractDeChargingProcedures: ExtractDeChargingProcedures,
    private val vehicleCoreRepository: VehicleCoreRepository
) : PagingSource<Int, DeChargingProcedure>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DeChargingProcedure> {
        try {
            val offset = params.key ?: 0
            val vin =
                vehicleCoreRepository.selectedVehicle.firstOrNull() ?: error("No vehicle selected!")
            val chargeTrackingPoints = getOffsetChargeTrackingPoints(
                GetOffsetChargeTrackingPointsParams(
                    vin,
                    offset,
                    AMOUNT_PER_PAGE
                )
            ).get()
            val chargingProcedures = extractDeChargingProcedures(chargeTrackingPoints).get()
            return LoadResult.Page(
                data = chargingProcedures,
                null,
                if(chargeTrackingPoints.isEmpty()) null else offset + chargeTrackingPoints.size
            )
        } catch (error: Exception) {
            TODO()
        }
    }

    companion object {
        private const val AMOUNT_PER_PAGE = 100
    }
}