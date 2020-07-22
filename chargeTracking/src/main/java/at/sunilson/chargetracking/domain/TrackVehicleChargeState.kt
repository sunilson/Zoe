package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.data.ChargeTrackingDao
import at.sunilson.chargetracking.data.toDatabaseEntity
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class TrackVehicleChargeState @Inject constructor(
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val chargeTrackingDao: ChargeTrackingDao
) : AsyncUseCase<Unit, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val batteryStatus = vehicleCoreService.getBatteryStatus(
            vehicleCoreRepository.kamereonAccountID,
            params
        ).toEntity()

        chargeTrackingDao.insertChargeTrackingPoint(
            ChargeTrackingPoint(
                params,
                System.currentTimeMillis(),
                batteryStatus
            ).toDatabaseEntity()
        )
    }
}