package at.sunilson.zoe.deviceControl

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.ControlAction
import android.service.controls.templates.StatelessTemplate
import android.widget.Toast
import androidx.annotation.RequiresApi
import at.sunilson.ktx.context.showToast
import at.sunilson.vehiclecore.domain.GetAllVehicles
import at.sunilson.vehiclecore.domain.HVACPreferences
import at.sunilson.vehiclecore.domain.StartHVAC
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.jdk9.asPublisher
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import java.util.function.Consumer
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class DeviceControlService : ControlsProviderService(), CoroutineScope {

    @Inject
    lateinit var getAllVehicles: GetAllVehicles

    @Inject
    lateinit var startHVAC: StartHVAC

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    private lateinit var updateFlow: MutableSharedFlow<Control>
    private var vehicleJob: Job? = null

    private val pi: PendingIntent
        get() = PendingIntent.getActivity(
            baseContext,
            1,
            Intent(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        updateFlow = MutableSharedFlow()

        vehicleJob?.cancel()
        vehicleJob = launch {
            getAllVehicles(Unit).collect { vehicles ->
                vehicles
                    .filter { controlIds.contains(it.vin) }
                    .forEach { vehicle ->
                        updateFlow.emit(
                            Control
                                .StatefulBuilder(vehicle.vin, pi)
                                .setTitle(vehicle.vin)
                                .setSubtitle(getString(R.string.start_pre_hvac))
                                .setStructure(getString(R.string.car))
                                .setDeviceType(DeviceTypes.TYPE_AC_HEATER)
                                .setControlTemplate(StatelessTemplate(vehicle.vin))
                                .setStatus(Control.STATUS_OK)
                                .build()
                        )
                    }
            }
        }

        return updateFlow.asPublisher(coroutineContext)
    }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return getAllVehicles(Unit).flatMapLatest {
            val controls = it.map { vehicle ->
                Control
                    .StatelessBuilder(vehicle.vin, pi)
                    .setTitle(vehicle.vin)
                    .setSubtitle(getString(R.string.start_pre_hvac))
                    .setStructure(getString(R.string.car))
                    .setDeviceType(DeviceTypes.TYPE_AC_HEATER)
                    .build()
            }
            flow { controls.forEach { control -> emit(control) } }
        }.asPublisher()
    }

    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        launch {
            startHVAC(HVACPreferences(22)).fold(
                { baseContext.showToast(R.string.hvac_started, Toast.LENGTH_LONG) },
                { baseContext.showToast(R.string.hvac_not_started, Toast.LENGTH_LONG) }
            )
        }
    }
}