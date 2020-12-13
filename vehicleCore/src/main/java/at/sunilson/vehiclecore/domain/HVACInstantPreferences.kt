package at.sunilson.vehiclecore.domain

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.remove
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.presentationcore.extensions.formatPattern
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject

data class HVACPreferences(val temperature: Int, val time: LocalTime? = null)


class SaveHVACInstantPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val vehicleCoreRepository: VehicleCoreRepository
) : AsyncUseCase<Unit, HVACPreferences>() {

    override suspend fun run(params: HVACPreferences) = SuspendableResult.of<Unit, Exception> {
        val vin =
            vehicleCoreRepository.selectedVehicle.firstOrNull() ?: error("No vehicle selected!")

        val temperature = preferencesKey<Int>("hvacTargetTemperature$vin")
        val time = preferencesKey<String>("hvacTargetTime$vin")

        dataStore.edit {
            it[temperature] = params.temperature
            if (params.time != null) {
                it[time] = params.time.formatPattern("HH:mm")
            } else {
                it.remove(time)
            }
        }
    }
}

class GetHVACInstantPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val vehicleCoreRepository: VehicleCoreRepository
) : FlowUseCase<HVACPreferences, Unit>() {
    override fun run(params: Unit) = vehicleCoreRepository
        .selectedVehicle
        .flatMapLatest { vin ->
            dataStore
                .data
                .map { preferences ->
                    val temperature = preferencesKey<Int>("hvacTargetTemperature$vin")
                    val time = preferencesKey<String>("hvacTargetTime$vin")

                    HVACPreferences(
                        preferences[temperature] ?: 21,
                        if (preferences[time] != null) LocalTime.parse(preferences[time]) else null
                    )
                }
        }
}