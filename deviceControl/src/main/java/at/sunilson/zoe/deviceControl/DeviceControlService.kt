package at.sunilson.zoe.deviceControl

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.BooleanAction
import android.service.controls.actions.ControlAction
import android.service.controls.templates.StatelessTemplate
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.jdk9.asPublisher
import java.util.concurrent.Flow
import java.util.function.Consumer
import kotlin.coroutines.CoroutineContext

@RequiresApi(Build.VERSION_CODES.R)
class DeviceControlService : ControlsProviderService(), CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    private lateinit var updateFlow: MutableSharedFlow<Control>

    private val pi: PendingIntent
        get() = PendingIntent.getActivity(
            baseContext,
            1,
            Intent(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {

        updateFlow = MutableSharedFlow()

        if (controlIds.contains("VIN")) {
            val control = Control
                .StatefulBuilder("VIN", pi)
                .setTitle("TODO")
                .setSubtitle("TODO")
                .setStructure("TODO")
                .setDeviceType(DeviceTypes.TYPE_AC_HEATER)
                .setControlTemplate(StatelessTemplate("TODO"))
                .setStatus(Control.STATUS_OK)
                .build()

            updateFlow.tryEmit(control)
        }

        return updateFlow.asPublisher(coroutineContext)
    }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        val control = Control
            .StatelessBuilder("VIN", pi)
            .setTitle("TODO")
            .setSubtitle("TODO")
            .setStructure("TODO")
            .setDeviceType(DeviceTypes.TYPE_AC_HEATER)
            .build()

        return flowOf(control).asPublisher(coroutineContext)
    }

    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        if (controlId == "VIN" && action is BooleanAction) {
            consumer.accept(ControlAction.RESPONSE_OK)

            val control = Control
                .StatefulBuilder("VIN", pi)
                .setTitle("TODO")
                .setSubtitle("TODO")
                .setStructure("TODO")
                .setDeviceType(DeviceTypes.TYPE_AC_HEATER)
                .setControlTemplate(StatelessTemplate("TODO"))
                .setStatus(Control.STATUS_OK)
                .build()

            updateFlow.tryEmit(control)
        }
    }
}