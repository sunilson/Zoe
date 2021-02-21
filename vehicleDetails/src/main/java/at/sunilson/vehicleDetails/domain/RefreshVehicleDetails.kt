package at.sunilson.vehicleDetails.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.collections.listBuilder
import at.sunilson.vehicleDetails.data.VehicleDetailsDao
import at.sunilson.vehicleDetails.data.VehicleDetailsService
import at.sunilson.vehicleDetails.data.models.DatabaseVehicleDetails
import at.sunilson.vehicleDetails.domain.entities.VehicleDetailsEntry
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

internal class RefreshVehicleDetails @Inject constructor(
    private val vehicleDetailsService: VehicleDetailsService,
    private val vehicleCoreRepository: VehicleCoreRepository,
    private val vehicleDao: VehicleDao,
    private val vehicleDetailsDao: VehicleDetailsDao
) : AsyncUseCase<Unit, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val result =
            vehicleDetailsService.getVehicleDetails(vehicleCoreRepository.kamereonAccountID, params)

        val entries = listBuilder<VehicleDetailsEntry> {
            add(VehicleDetailsEntry.Information("brand", "Marke", result.brand["label"]!!))
            add(
                VehicleDetailsEntry.Information(
                    "commercialModel",
                    "Modell",
                    result.commercialModel["label"]!!
                )
            )
            add(VehicleDetailsEntry.Information("energy", "Energie", result.energy["label"]!!))
            add(VehicleDetailsEntry.Information("vin", "VIN", result.vin))
            addAll(result.equipments.map { equipment ->
                VehicleDetailsEntry.Equipment(equipment.code, equipment.group, equipment.label)
            })
        }

        vehicleDetailsDao.insertDetails(DatabaseVehicleDetails(params, entries))
    }
}
