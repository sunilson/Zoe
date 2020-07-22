package at.sunilson.vehicle.data

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.vehicle.domain.VehicleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    VehicleRepository {
    override var selectedVehicle: String?
        get() = sharedPreferences.getString(SELECTED_VEHICLE, null)
        set(value) {
            sharedPreferences.edit { putString(SELECTED_VEHICLE, value) }
        }

    companion object {
        const val SELECTED_VEHICLE = "selectedVehicle"
    }
}