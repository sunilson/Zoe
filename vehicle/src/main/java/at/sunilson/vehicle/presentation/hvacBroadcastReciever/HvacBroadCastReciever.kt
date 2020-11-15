package at.sunilson.vehicle.presentation.hvacBroadcastReciever

import android.content.Context
import android.content.Intent
import at.sunilson.core.base.BaseBroadCastReceiver
import at.sunilson.ktx.context.showToast
import at.sunilson.vehicle.domain.StartClimateControl
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HvacBroadCastReciever : BaseBroadCastReceiver() {

    @Inject
    internal lateinit var startClimateControl: StartClimateControl

    @Inject
    lateinit var vehicleCoreRepository: VehicleCoreRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        runBlocking {
            val selectedVehicle =
                vehicleCoreRepository.selectedVehicle.firstOrNull() ?: return@runBlocking
            startClimateControl(selectedVehicle).fold(
                { context.showToast("Klimatisierungs Anfrage gesendet!") },
                {
                    context.showToast("Klimatisierungs Anfrage konnte nicht gesendet werden!")
                    Timber.e(it)
                }
            )
        }
    }
}