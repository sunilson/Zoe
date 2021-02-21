package at.arkulpa.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import at.sunilson.vehicleMap.domain.GetReachableArea
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReachableAreaWidgetProvider : AppWidgetProvider() {

    private var updateJob: Job? = null

    @Inject
    lateinit var getReachableArea: GetReachableArea

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        val remoteViews = RemoteViews(context.packageName, R.layout.reachable_area_widget_layout)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        updateJob?.cancel()
        updateJob = GlobalScope.launch(Dispatchers.Main) {
            // val reachableArea = getReachableArea(Unit).firstOrNull() ?: return@launch
            appWidgetIds?.forEach { widgetId ->
                val options = appWidgetManager.getAppWidgetOptions(widgetId)
                val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
                val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            }
        }
    }
}
