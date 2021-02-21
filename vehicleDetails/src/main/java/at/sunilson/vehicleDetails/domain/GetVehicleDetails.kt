package at.sunilson.vehicleDetails.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.ktx.collections.listBuilder
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
                val entities = databaseDetails
                    ?.vehicleDetails
                    ?.toMutableList()
                    ?: listOf<VehicleDetailsEntry>()

                val informationEntries =
                    entities.filterIsInstance<VehicleDetailsEntry.Information>()
                val equipmentEntries = entities.filterIsInstance<VehicleDetailsEntry.Equipment>()

                listBuilder<VehicleDetailsEntry> {
                    add(imageEntry)
                    addAll(informationEntries)
                    addAll(
                        equipmentEntries
                            .groupBy { it.group }
                            .flatMap { it.value.sortedBy { it.label } }
                    )
                }
            }
        }
}
