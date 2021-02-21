package at.sunilson.vehiclecore.data

import android.content.SharedPreferences
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleCoreRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dataStore: DataStore<Preferences>
) : VehicleCoreRepository {
    private val selectedVehicleKey = preferencesKey<String>("selectedVehicle")

    override val kamereonAccountID: String
        get() = requireNotNull(
            sharedPreferences.getString(
                AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID,
                null
            )
        )

    override val selectedVehicle: Flow<String?>
        get() = dataStore.data.map { it[selectedVehicleKey] }

    override suspend fun setSelectedVehicle(vin: String) {
        dataStore.edit { it[selectedVehicleKey] = vin }
    }
}
