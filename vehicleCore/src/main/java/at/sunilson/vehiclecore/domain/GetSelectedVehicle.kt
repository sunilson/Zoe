package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSelectedVehicle @Inject constructor(
    private val vehicleRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao
) : FlowUseCase<Vehicle?, Unit>() {
    override fun run(params: Unit) =
        vehicleRepository.selectedVehicle.flatMapMerge { selectedVehicleVin ->
            if (selectedVehicleVin.isNullOrEmpty()) {
                vehicleDao.getAllVehicles().map { vehicles ->
                    vehicles.firstOrNull()?.apply { vehicleRepository.setSelectedVehicle(vin) }
                }
            } else {
                vehicleDao.getVehicle(selectedVehicleVin)
            }
        }.map { it?.toEntity() }.distinctUntilChanged()
}
