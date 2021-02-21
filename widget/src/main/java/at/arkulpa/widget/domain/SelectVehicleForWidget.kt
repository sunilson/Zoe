package at.arkulpa.widget.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import timber.log.Timber
import javax.inject.Inject

data class SelectVehicleForWidgetParams(val vin: String, val widgetId: String)

internal class SelectVehicleForWidget @Inject constructor(private val sharedPreferences: SharedPreferences) :
    UseCase<Unit, SelectVehicleForWidgetParams>() {
    override fun run(params: SelectVehicleForWidgetParams) = Result.of<Unit, Exception> {
        sharedPreferences.edit {
            Timber.d("Setting vin ${params.vin} for widget ${params.widgetId}")
            putString("Widget ${params.widgetId}", params.vin)
        }
    }
}
