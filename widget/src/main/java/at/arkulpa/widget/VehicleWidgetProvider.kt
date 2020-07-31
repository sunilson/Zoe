package at.arkulpa.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.CallSuper
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

abstract class HiltBroadcastReceiver : AppWidgetProvider() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}

@AndroidEntryPoint
class VehicleWidgetProvider : HiltBroadcastReceiver() {

    @Inject
    lateinit var vehicleCoreRepository: VehicleCoreRepository

    @Inject
    lateinit var vehicleDao: VehicleDao

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        runBlocking {
            Log.d("Linus", vehicleCoreRepository.toString())
            Log.d("Linus", vehicleCoreRepository.selectedVehicle.toString())
            Log.d("Linus", vehicleDao.toString())
            val selectedVehicle = vehicleCoreRepository.selectedVehicle ?: return@runBlocking
            val vehicle = vehicleDao.getVehicle(selectedVehicle).first()?.toEntity()

            appWidgetIds?.forEach { widgetId ->
                val remoteViews = RemoteViews(context.packageName, R.layout.vehicle_widget_layout)
                remoteViews.setTextViewText(R.id.vehicle_name, vehicle?.modelName)
                appWidgetManager?.updateAppWidget(widgetId, remoteViews)
            }
        }
    }
}