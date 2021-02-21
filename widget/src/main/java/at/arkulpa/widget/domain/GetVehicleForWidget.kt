package at.arkulpa.widget.domain

import android.content.SharedPreferences
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.GetVehicle
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class GetVehicleForWidget @Inject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val getVehicle: GetVehicle,
    private val sharedPreferences: SharedPreferences
) : AsyncUseCase<Vehicle, String>() {
    override suspend fun run(params: String) = SuspendableResult.of<Vehicle, Exception> {
        val vin = sharedPreferences.getString(params, null)
        if (vin == null) {
            getSelectedVehicle(Unit).firstOrNull()!!
        } else {
            getVehicle(vin).get()!!
        }
    }
}
