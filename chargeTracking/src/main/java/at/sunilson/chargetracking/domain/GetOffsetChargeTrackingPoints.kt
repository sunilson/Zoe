package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.data.ChargeTrackingDao
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

data class GetOffsetChargeTrackingPointsParams(
    val vin: String,
    val offset: Int,
    val amountPerPage: Int
)

class GetOffsetChargeTrackingPoints @Inject constructor(private val chargeTrackingDao: ChargeTrackingDao) :
    AsyncUseCase<List<ChargeTrackingPoint>, GetOffsetChargeTrackingPointsParams>() {

    override suspend fun run(params: GetOffsetChargeTrackingPointsParams) =
        SuspendableResult.of<List<ChargeTrackingPoint>, Exception> {
            chargeTrackingDao.getChargeTrackingPoints(
                params.vin,
                params.offset,
                params.amountPerPage
            ).map {
                ChargeTrackingPoint(
                    it.vehicleId,
                    it.timestamp,
                    it.batteryStatus,
                    it.mileageKm,
                    it.location
                )
            }
        }
}
