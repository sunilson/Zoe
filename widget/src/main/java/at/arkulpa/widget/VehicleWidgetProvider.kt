package at.arkulpa.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.widget.RemoteViews
import at.arkulpa.widget.domain.GetVehicleForWidget
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehiclecore.presentation.extensions.displayName
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.github.kittinunf.result.coroutines.success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VehicleWidgetProvider : AppWidgetProvider() {

    private var updateJob: Job? = null

    @Inject
    internal lateinit var getVehicleForWidget: GetVehicleForWidget

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        val options = appWidgetManager.getAppWidgetOptions(appWidgetId) ?: return
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        val remoteViews = RemoteViews(context.packageName, R.layout.vehicle_widget_layout)
        if (minWidth < 100) {
            remoteViews.setViewVisibility(R.id.vehicle_image, GONE)
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        updateJob?.cancel()
        updateJob = GlobalScope.launch(Dispatchers.Main) {
            appWidgetIds?.forEach { widgetId ->
                try {
                    Timber.d("Loading widget info for Widget $widgetId")
                    getVehicleForWidget(widgetId.toString()).success { vehicle ->
                        Timber.d("Loaded vehicle for widghet $widgetId: $vehicle")
                        val remoteViews =
                            RemoteViews(context.packageName, R.layout.vehicle_widget_layout)
                        remoteViews.setTexts(context, vehicle)
                        remoteViews.setupClickIntents(context, vehicle)
                        remoteViews.loadVehicleImage(context, vehicle)
                        appWidgetManager?.updateAppWidget(widgetId, remoteViews)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error updating widget $widgetId")
                }
            }
        }
    }

    private fun RemoteViews.setTexts(context: Context, vehicle: Vehicle) {
        setTextViewText(R.id.vehicle_name, vehicle.modelName)

        setTextViewText(
            R.id.vehicle_battery,
            "${vehicle.batteryStatus.batteryLevel} % (${vehicle.batteryStatus.remainingRange} km)"
        )

        setTextViewText(
            R.id.vehicle_charging_state,
            context.getString(vehicle.batteryStatus.chargeState.displayName)
        )

        setTextViewText(R.id.vehicle_mileage, "Kilometerstand: ${vehicle.mileageKm} km")
    }

    private suspend fun RemoteViews.loadVehicleImage(context: Context, vehicle: Vehicle) {
        val request = ImageRequest.Builder(context)
            .data(vehicle.imageUrl)
            .allowHardware(false)
            .build()
        val result = (Coil.imageLoader(context).execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        setImageViewBitmap(R.id.vehicle_image, bitmap)
    }

    private fun RemoteViews.setupClickIntents(context: Context, selectedVehicle: Vehicle) {
        val homeIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse("zoe://vehicle_overview/${selectedVehicle.vin}")
        }

        val pendingHomeIntent =
            PendingIntent.getActivity(context, 1, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.widget_root, pendingHomeIntent)

        val hvacIntent = Intent().apply {
            component = ComponentName(
                "at.sunilson.zoe",
                "at.sunilson.vehicle.presentation.hvacBroadcastReciever.HvacBroadCastReciever"
            )
            action = "StartHVAC"
        }
        val pendingHvacIntent =
            PendingIntent.getBroadcast(context, 1, hvacIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.start_hvac, pendingHvacIntent)

        val mapIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse("zoe://vehicle_location/${selectedVehicle.vin}")
        }
        val pendingMapIntent =
            PendingIntent.getActivity(context, 1, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.show_vehicle_location, pendingMapIntent)


        val chargeIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse("zoe://charge_statistics/${selectedVehicle.vin}")
        }
        val pendingChargeIntent =
            PendingIntent.getActivity(context, 1, chargeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.show_charge_statistics, pendingChargeIntent)
    }

    companion object {
        const val UPDATE_WIDGET_ID = "updateWidgetId"
    }
}