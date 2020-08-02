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
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.toEntity
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.Coil
import coil.request.GetRequest
import coil.request.SuccessResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VehicleWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var vehicleCoreRepository: VehicleCoreRepository

    @Inject
    lateinit var vehicleDao: VehicleDao

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

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
        CoroutineScope(Dispatchers.Main).launch {
            val selectedVehicle = vehicleCoreRepository.selectedVehicle ?: return@launch
            val vehicle =
                vehicleDao.getVehicle(selectedVehicle).first()?.toEntity() ?: return@launch

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
            "${vehicle.batteryStatus.batteryLevel} % (${vehicle.batteryStatus.availableEnery} kWh)"
        )

        setTextViewText(
            R.id.vehicle_range,
            "Reichweite: ${vehicle.batteryStatus.remainingRange} km"
        )

        setTextViewText(R.id.vehicle_mileage, "Kilometerstand: ${vehicle.mileageKm} km")
    }

    private suspend fun RemoteViews.loadVehicleImage(context: Context, vehicle: Vehicle) {
        val request = GetRequest.Builder(context)
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
        setOnClickPendingIntent(R.id.vehicle_image, pendingHomeIntent)

        val hvacIntent = Intent(Intent.ACTION_VIEW, Uri.parse("zoe://vehicle_overview/start_hvac"))
        val pendingHvacIntent =
            PendingIntent.getActivity(context, 1, hvacIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        setOnClickPendingIntent(R.id.start_hvac, pendingHvacIntent)

        val mapIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            component = ComponentName("at.sunilson.zoe", "at.sunilson.zoe.MainActivity")
            data = Uri.parse(
                if (selectedVehicle == null) {
                    "zoe://vehicle_overview/vehicle_location"
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
                    "zoe://vehicle_overview/charge_statistics"
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