package at.sunilson.vehicle.presentation.hvacBroadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import at.sunilson.core.base.BaseBroadCastReceiver
import at.sunilson.ktx.context.showToast
import at.sunilson.vehicle.domain.StartClimateControl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HvacBroadCastReciever : BaseBroadCastReceiver() {

    @Inject
    internal lateinit var startClimateControl: StartClimateControl

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        runBlocking {
            startClimateControl(Unit).fold(
                { context.showToast("Klimatisierungs Anfrage gesendet!") },
                {
                    context.showToast("Klimatisierungs Anfrage konnte nicht gesendet werden!")
                    Timber.e(it)
                }
            )
        }
    }
}