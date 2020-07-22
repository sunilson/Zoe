package at.sunilson.vehiclecore.data

import android.content.SharedPreferences
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
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
}