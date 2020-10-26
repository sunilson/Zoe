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
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import at.sunilson.vehiclecore.domain.entities.Vehicle
import at.sunilson.vehiclecore.presentation.extensions.displayName
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VehicleWidgetProvider : AppWidgetProvider() {

    private var updateJob: Job? = null

    @Inject
    lateinit var vehicleCoreRepository: VehicleCoreRepository

    @Inject
    lateinit var getSelectedVehicle: GetSelectedVehicle

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        //TODO https://stackoverflow.com/questions/17138191/how-to-make-android-widget-layout-responsive

        val options = appWidgetManager.getAppWidgetOptions(appWidgetId) ?: return
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        val remoteViews = RemoteViews(context.packageName, R.layout.vehicle_widget_layout)
        if (minWidth < 100) {
            remoteViews.setViewVisibility(R.id.vehicle_image, GONE)
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        updateJob?.cancel()
        updateJob = GlobalScope.launch(Dispatchers.Main) {
            val vehicle = getSelectedVehicle(Unit).first() ?: return@launch
            appWidgetIds?.forEach { widgetId ->
                val remoteViews = RemoteViews(context.packageName, R.layout.vehicle_widget_layout)
                remoteViews.setTexts(context, vehicle)
                remoteViews.setupClickIntents(context)
                remoteViews.loadVehicleImage(context, vehicle)

                appWidgetManager?.updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    private fun RemoteViews.setTexts(context: Context, vehicle: Vehicle) {
        setTextViewText(R.id.vehicle_name, vehicle.modelName)

        setTextViewText(
            R.id.vehicle_battery,
            "${vehicle.batteryStatus.batteryLevel} % (${vehicle.batteryStatus.remainingRange} km"
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

    private fun RemoteViews.setupClickIntents(context: Context) {
        val selectedVehicle = vehicleCoreRepository.selectedVehicle

        val homeIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse("zoe://vehicle_overview")
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
            data = Uri.parse(
                if (selectedVehicle == null) {
                    "zoe://vehicle_overview"
                } else {
                    "zoe://vehicle_location/$selectedVehicle"
                }
            )
        }
        val pendingMapIntent =
            PendingIntent.getActivity(context, 1, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.show_vehicle_location, pendingMapIntent)


        val chargeIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse(
                if (selectedVehicle == null) {
                    "zoe://vehicle_overview"
                } else {
                    "zoe://charge_statistics/$selectedVehicle"
                }
            )
        }
        val pendingChargeIntent =
            PendingIntent.getActivity(context, 1, chargeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.show_charge_statistics, pendingChargeIntent)
    }
}