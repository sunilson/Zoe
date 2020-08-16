package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.data.ChargeTrackingDao
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllChargeTrackingPoints @Inject constructor(private val chargeTrackingDao: ChargeTrackingDao) :
    FlowUseCase<List<ChargeTrackingPoint>, String>() {
    override fun run(params: String) =
        chargeTrackingDao.getAllChargeTrackingPoints(params).distinctUntilChanged()
            .map {
                it.map {
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