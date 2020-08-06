package at.sunilson.vehiclecore.data

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleCoreRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    VehicleCoreRepository {
    override val kamereonAccountID: String
        get() = requireNotNull(
            sharedPreferences.getString(
                AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID,
                null
            )
        )

    override var selectedVehicle: String?
        get() = sharedPreferences.getString(SELECTED_VEHICLE, null)
        set(value) {
            sharedPreferences.edit { putString(SELECTED_VEHICLE, value) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val selectedVehicleFlow: Flow<String?>
        get() = callbackFlow {
            offer(sharedPreferences.getString(SELECTED_VEHICLE, null))
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
                if (key == SELECTED_VEHICLE) {
                    offer(sp.getString(key, null))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
        }.distinctUntilChanged()

    companion object {
        const val SELECTED_VEHICLE = "selectedVehicle"
    }
}