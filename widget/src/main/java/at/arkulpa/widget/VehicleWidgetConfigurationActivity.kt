package at.arkulpa.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import at.arkulpa.widget.databinding.VehicleWidgetConfigurationActivityBinding
import at.sunilson.ktx.window.useLightNavigationBarIcons
import at.sunilson.ktx.window.useLightStatusBarIcons
import at.sunilson.presentationcore.extensions.getThemeColor
import at.sunilson.presentationcore.extensions.nightMode
import at.sunilson.vehiclecore.presentation.vehicleListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class VehicleWidgetConfigurationActivity : AppCompatActivity() {

    private val viewModel by viewModels<VehicleWidgetConfigurationViewModel>()
    private var binding: VehicleWidgetConfigurationActivityBinding? = null

    private val widgetID by lazy {
        intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (nightMode) {
            window.statusBarColor = getThemeColor(R.attr.colorSurface)
            window.navigationBarColor = getThemeColor(R.attr.colorSurface)
        } else {
            window.statusBarColor = getColor(android.R.color.white)
            window.navigationBarColor = getColor(android.R.color.white)
        }

        window.useLightNavigationBarIcons(nightMode)
        window.useLightStatusBarIcons(nightMode)

        //Widget wont be added if user leaves activity without confirming
        setResult(
            Activity.RESULT_CANCELED,
            Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID) }
        )

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        binding = VehicleWidgetConfigurationActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        observeState()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun observeState() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                binding?.vehicleList?.withModels {
                    state.vehicles.forEach { vehicle ->
                        vehicleListItem {
                            id(vehicle.vin)
                            vehicle(vehicle)
                            onVehicleClick {
                                viewModel.vehicleSelected(vehicle, widgetID)
                                updateWidget()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateWidget() {
        sendBroadcast(Intent().apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(VehicleWidgetProvider.UPDATE_WIDGET_ID, widgetID)
        })

        setResult(
            Activity.RESULT_OK,
            Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID) }
        )

        finish()
    }
}