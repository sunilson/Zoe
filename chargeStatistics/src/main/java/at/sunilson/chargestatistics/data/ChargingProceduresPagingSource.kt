package at.sunilson.chargestatistics.data

import androidx.paging.PagingSource
import at.sunilson.chargestatistics.domain.ExtractChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.GetOffsetChargeTrackingPoints
import at.sunilson.chargetracking.domain.GetOffsetChargeTrackingPointsParams
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

internal class ChargingProceduresPagingSource @Inject constructor(
    private val getOffsetChargeTrackingPoints: GetOffsetChargeTrackingPoints,
    private val extractChargingProcedures: ExtractChargingProcedures,
    private val vehicleCoreRepository: VehicleCoreRepository
) : PagingSource<Int, ChargingProcedure>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChargingProcedure> {
        return try {
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
            val chargingProcedures = extractChargingProcedures(chargeTrackingPoints).get()
            LoadResult.Page(
                data = chargingProcedures,
                null,
                if (chargeTrackingPoints.isEmpty()) null else offset + chargeTrackingPoints.size
            )
        } catch (error: Exception) {
            Timber.e(error)
            LoadResult.Error(error)
        }
    }

    companion object {
        private const val AMOUNT_PER_PAGE = 100
    }
}
