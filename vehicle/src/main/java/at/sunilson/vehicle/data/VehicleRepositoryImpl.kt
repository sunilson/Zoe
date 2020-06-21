package at.sunilson.vehicle.data

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.database.VehicleDao
import at.sunilson.database.mappers.toDatabaseEntity
import at.sunilson.database.mappers.toEntity
import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.domain.VehicleRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val vehicleService: VehicleService,
    private val sharedPreferences: SharedPreferences,
    private val vehicleDao: VehicleDao
) : VehicleRepository {
    override var selectedVehicle: String?
        get() = sharedPreferences.getString(SELECTED_VEHICLE, null)
        set(value) {
            sharedPreferences.edit { putString(SELECTED_VEHICLE, value) }
        }

    override suspend fun saveVehiclesToLocalStorage(vehicles: List<Vehicle>) =
        SuspendableResult.of<Unit, Exception> {
            vehicleDao.upsertVehicles(vehicles.map { it.toDatabaseEntity() })
        }

    override suspend fun getRefreshedVehicles() = SuspendableResult.of<List<Vehicle>, Exception> {
        val accountId = requireNotNull(
            sharedPreferences.getString(
                AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID,
                null
            )
        )

        vehicleService.getAllVehicles(accountId).toVehicleList()
    }

    override fun getVehicle(id: String) = vehicleDao.getVehicle(id).map { it?.toEntity() }

    override fun getAllVehicles() = vehicleDao.getAllVehicles().map { it.map { it.toEntity() } }

    companion object {
        const val SELECTED_VEHICLE = "selectedVehicle"
    }
}