package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class TrackVehicleChargeState @Inject constructor(
    private val vehicleCoreService: VehicleCoreService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val createVehicleChargePoint: CreateVehicleChargePoint
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

            createVehicleChargePoint(
                CreateChargePointParams(
                    params,
                    batteryStatus,
                    mileage,
                    location
                )
            ).get()
        }
}