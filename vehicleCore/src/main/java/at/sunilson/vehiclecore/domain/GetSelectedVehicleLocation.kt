package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.entities.Location
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSelectedVehicleLocation @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val getSelectedVehicle: GetSelectedVehicle
) : FlowUseCase<Location?, Unit>() {
    override fun run(params: Unit) = getSelectedVehicle(Unit)
        .flatMapLatest { vehicle ->
            if (vehicle != null) {
                vehicleDao.getVehicleLocation(vehicle.vin).map { it?.toEntity() }
            } else {
                flowOf()
            }
        }
        .distinctUntilChanged()
}