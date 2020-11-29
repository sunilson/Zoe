package at.sunilson.vehicle.presentation.hvacBroadcastReciever

import android.content.Context
import android.content.Intent
import at.sunilson.core.base.BaseBroadCastReceiver
import at.sunilson.ktx.context.showToast
import at.sunilson.vehicle.R
import at.sunilson.vehicle.domain.GetHVACInstantPreferences
import at.sunilson.vehicle.domain.StartHVAC
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HvacBroadCastReciever : BaseBroadCastReceiver() {

    @Inject
    internal lateinit var startHVAC: StartHVAC

    @Inject
    internal lateinit var getHVACInstantPreferences: GetHVACInstantPreferences

    @Inject
    lateinit var vehicleCoreRepository: VehicleCoreRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        runBlocking {
            val preferences = getHVACInstantPreferences(Unit).firstOrNull() ?: return@runBlocking
            startHVAC(preferences).fold(
                { context.showToast(context.getString(R.string.hvac_started)) },
                {
                    context.showToast(context.getString(R.string.hvac_not_started))
                    Timber.e(it)
                }
            )
        }
    }
}