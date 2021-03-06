package at.sunilson.chargetracking.domain

import at.sunilson.chargetracking.data.ChargeTrackingDao
import at.sunilson.chargetracking.data.toDatabaseEntity
import at.sunilson.chargetracking.data.toEntity
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toDatabaseEntity
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class CreateChargePointParams(
    val vin: String,
    val batteryStatus: Vehicle.BatteryStatus,
    val mileage: Int,
    val location: Location?
)

class CreateVehicleChargePoint @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val chargeTrackingDao: ChargeTrackingDao
) : AsyncUseCase<ChargeTrackingPoint, CreateChargePointParams>() {

    override suspend fun run(params: CreateChargePointParams) =
        SuspendableResult.of<ChargeTrackingPoint, Exception> {
            val (vin, batteryStatus, mileage, location) = params

            // Update existing vehicle
            val previousVehicle = vehicleDao.getVehicle(vin).first()?.toEntity()

            previousVehicle?.copy(batteryStatus = batteryStatus, mileageKm = mileage)?.let {
                vehicleDao.upsertVehicles(
                    listOf(
                        if (previousVehicle == it) {
                            it
                        } else {
                            it.copy(lastChangeTimestamp = System.currentTimeMillis())
                        }.toDatabaseEntity()
                    )
                )
            }

            val previousTrackingPoint =
                chargeTrackingDao.getLatestChargeTrackingPoint(params.vin)?.toEntity()
            val trackingPoint = ChargeTrackingPoint(
                vin,
                System.currentTimeMillis(),
                batteryStatus,
                mileage,
                location
            )

            // Only insert if not the same or 14 minutes passed
            if (previousTrackingPoint == null ||
                !previousTrackingPoint.compareTimeIndependent(trackingPoint) ||
                trackingPoint.timestamp - previousTrackingPoint.timestamp >= TRACKING_POINT_MINUTE_THRESHOLD
            ) {
                chargeTrackingDao.insertChargeTrackingPoint(trackingPoint.toDatabaseEntity())
            }

            trackingPoint
        }

    companion object {
        const val TRACKING_POINT_MINUTE_THRESHOLD = 14 * 60 * 1000
    }
}
