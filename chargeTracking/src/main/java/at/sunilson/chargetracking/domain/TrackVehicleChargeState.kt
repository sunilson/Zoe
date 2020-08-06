package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.data.ChargeTrackingDao
import at.sunilson.chargetracking.data.toDatabaseEntity
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TrackVehicleChargeState @Inject constructor(
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val chargeTrackingDao: ChargeTrackingDao,
    private val vehicleDao: VehicleDao
) : AsyncUseCase<ChargeTrackingPoint, String>() {
    override suspend fun run(params: String) =
        SuspendableResult.of<ChargeTrackingPoint, Exception> {
            val batteryStatus = vehicleCoreService.getBatteryStatus(
                vehicleCoreRepository.kamereonAccountID,
                params
            ).toEntity()

            val mileage = vehicleCoreService.getKilometerReading(
                vehicleCoreRepository.kamereonAccountID,
                params
            ).toEntity()

            val location = try {
                vehicleCoreService.getVehicleLocation(
                    vehicleCoreRepository.kamereonAccountID,
                    params
                ).toEntity()
            } catch (error: Exception) {
                null
            }

            //Update existing vehicle
            val previousVehicle = vehicleDao.getVehicle(params).first()?.toEntity()
            previousVehicle?.copy(batteryStatus = batteryStatus, mileageKm = mileage)?.let {
                vehicleDao.upsertVehicles(listOf(it.toDatabaseEntity()))
            }

            val trackingPoint = ChargeTrackingPoint(
                params,
                System.currentTimeMillis(),
                batteryStatus,
                mileage,
                location
            )

            chargeTrackingDao.insertChargeTrackingPoint(trackingPoint.toDatabaseEntity())

            trackingPoint
        }
}