package at.sunilson.vehicle.data

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.database.VehicleDao
import at.sunilson.database.mappers.toDatabaseEntity
import at.sunilson.database.mappers.toEntity
import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.data.entities.KamereonPostBody
import at.sunilson.vehicle.data.entities.batterystatus.BatteryStatusResponse
import at.sunilson.vehicle.data.entities.cockpit.CockpitResponse
import at.sunilson.vehicle.data.entities.location.LocationResponse
import at.sunilson.vehicle.domain.VehicleRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.kittinunf.result.coroutines.map
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    override val kamereonAccountID: String
        get() = requireNotNull(
            sharedPreferences.getString(
                AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID,
                null
            )
        )

    companion object {
        const val SELECTED_VEHICLE = "selectedVehicle"
    }
}