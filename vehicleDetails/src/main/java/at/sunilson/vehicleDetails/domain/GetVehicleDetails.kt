package at.sunilson.vehicleDetails.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehicleDetails.data.VehicleDetailsDao
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetVehicleDetails @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val vehicleDetailsDao: VehicleDetailsDao
) : FlowUseCase<List<VehicleDetailsEntry>, String>() {
    override fun run(params: String) =
        vehicleDao.getVehicle(params).flatMapLatest {
            val vehicle = it?.toEntity() ?: error("Vehicle was null!")
            val imageEntry = VehicleDetailsEntry.Image(vehicle.imageUrl)
            vehicleDetailsDao.getAllVehicleDetails(params).map { databaseDetails ->
                databaseDetails?.vehicleDetails?.toMutableList()?.apply { add(0, imageEntry) }
                    ?: listOf(imageEntry)
            }
        }

}