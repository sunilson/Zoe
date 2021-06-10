package at.sunilson.vehicle.presentation.vehicleOverview

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.appointments.domain.GetNearestAppointment
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.contracts.domain.GetNearestExpiringContract
import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.vehicle.domain.GetSelectedVehicleCurrentChargeProcedure
import at.sunilson.vehicle.domain.RefreshAllVehicles
import at.sunilson.vehicle.domain.SelectVehicle
import at.sunilson.vehicle.domain.StartCharging
import at.sunilson.vehicle.domain.StopHVAC
import at.sunilson.vehicle.domain.entities.ChargeProcedure
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

data class VehicleOverviewState(
    val loading: Boolean = false,
    val selectedVehicle: Vehicle? = null,
    val currentChargeProcedure: ChargeProcedure? = null,
    val nextAppointment: Appointment? = null,
    val nextContract: Contract? = null
)

sealed class VehicleOverviewSideEffects {
    object ShowSplashScreen : VehicleOverviewSideEffects()
    object NoVehiclesAvailable : VehicleOverviewSideEffects()
    object RequestFailed : VehicleOverviewSideEffects()
    object HVACStopped : VehicleOverviewSideEffects()
    object HVACNotStopped : VehicleOverviewSideEffects()
    data class ShowToast(val message: String) : VehicleOverviewSideEffects()
    data class ShowVehicleDetails(val vin: String, val imageUri: String) :
        VehicleOverviewSideEffects()

    data class ShowVehicleStatistics(val vin: String) : VehicleOverviewSideEffects()
    data class ShowChargeStatistics(val vin: String) : VehicleOverviewSideEffects()
    data class ShowVehicleLocation(val vin: String) : VehicleOverviewSideEffects()
}

@Suppress("LongParameterList")
@HiltViewModel
internal class VehicleOverviewViewModel @Inject constructor(
    private val getSelectedVehicle: GetSelectedVehicle,
    private val refreshAllVehicles: RefreshAllVehicles,
    private val startChargingUseCase: StartCharging,
    private val getSelectedVehicleCurrentChargeProcedure: GetSelectedVehicleCurrentChargeProcedure,
    private val getNearestAppointment: GetNearestAppointment,
    private val getNearestExpiringContract: GetNearestExpiringContract,
    private val selectVehicle: SelectVehicle,
    private val stopHVAC: StopHVAC
) : ViewModel(), ContainerHost<VehicleOverviewState, VehicleOverviewSideEffects> {

    override val container = container<VehicleOverviewState, VehicleOverviewSideEffects>(
        VehicleOverviewState()
    )

    private var selectedVehicleJob: Job? = null
    private var chargeProcedureJob: Job? = null
    private var nearestAppointmentJob: Job? = null
    private var nearestExpiringContractJob: Job? = null
    private var refreshingJob: Job? = null

    var themeImage: Bitmap? = null

    init {
        loadSelectedVehicle()
        refreshVehiclesRequested()
    }

    fun vehicleSelected(vin: String) = intent { selectVehicle(vin) }

    fun refreshVehiclesRequested(invisible: Boolean = false) = intent {
        refreshingJob?.cancel()
        refreshingJob = viewModelScope.launch {
            if (!invisible) reduce { state.copy(loading = true) }

            refreshAllVehicles(Unit).fold(
                {
                    if (it.isEmpty()) {
                        postSideEffect(VehicleOverviewSideEffects.NoVehiclesAvailable)
                    }
                },
                {
                    Timber.e(it)
                    if (state.selectedVehicle == null) {
                        postSideEffect(VehicleOverviewSideEffects.NoVehiclesAvailable)
                    } else {
                        postSideEffect(VehicleOverviewSideEffects.RequestFailed)
                    }
                }
            )

            reduce { state.copy(loading = false) }
        }
    }

    fun startChargingClicked(vin: String) = intent {
        startChargingUseCase(vin).fold(
            { postSideEffect(VehicleOverviewSideEffects.ShowToast("Lade Anfrage gesendet!")) },
            { Timber.e(it) }
        )
    }

    fun stopHVACClicked() = intent {
        stopHVAC(Unit).fold(
            { postSideEffect(VehicleOverviewSideEffects.HVACStopped) },
            { postSideEffect(VehicleOverviewSideEffects.HVACNotStopped) }
        )
    }

    fun showChargeStatistics() = intent {
        state.selectedVehicle?.let { vehicle ->
            postSideEffect(VehicleOverviewSideEffects.ShowChargeStatistics(vehicle.vin))
        }
    }

    fun showVehicleDetailsClicked() = intent {
        state.selectedVehicle?.let { vehicle ->
            postSideEffect(
                VehicleOverviewSideEffects.ShowVehicleDetails(
                    vehicle.vin,
                    vehicle.imageUrl
                )
            )
        }
    }

    private fun loadSelectedVehicle() = intent {
        selectedVehicleJob?.cancel()
        selectedVehicleJob = viewModelScope.launch {
            getSelectedVehicle(Unit).collect { vehicle ->
                if (vehicle == null) {
                    Timber.e("Selected Vehicle was null")
                    postSideEffect(VehicleOverviewSideEffects.ShowSplashScreen)
                } else {
                    reduce { state.copy(selectedVehicle = vehicle) }
                }
            }
        }

        chargeProcedureJob?.cancel()
        chargeProcedureJob = viewModelScope.launch {
            getSelectedVehicleCurrentChargeProcedure(Unit).collect {
                reduce { state.copy(currentChargeProcedure = it) }
            }
        }

        nearestAppointmentJob?.cancel()
        nearestAppointmentJob = viewModelScope.launch {
            getNearestAppointment(Unit).collect {
                reduce { state.copy(nextAppointment = it) }
            }
        }

        nearestExpiringContractJob?.cancel()
        nearestExpiringContractJob = viewModelScope.launch {
            getNearestExpiringContract(Unit).collect {
                reduce { state.copy(nextContract = it) }
            }
        }
    }
}
