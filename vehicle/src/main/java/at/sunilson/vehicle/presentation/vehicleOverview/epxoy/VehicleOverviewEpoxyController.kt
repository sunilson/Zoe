package at.sunilson.vehicle.presentation.vehicleOverview.epxoy

import at.sunilson.entities.Vehicle
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.batteryStatusWidget
import at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models.buttonWidget
import com.airbnb.epoxy.TypedEpoxyController

sealed class VehicleOverviewControllerEvents
object StartClimateControl : VehicleOverviewControllerEvents()
object ShowVehicleDetails : VehicleOverviewControllerEvents()
object ShowVehicleLocation : VehicleOverviewControllerEvents()
object ShowVehicleStatistics : VehicleOverviewControllerEvents()

class VehicleOverviewEpoxyController(private val onClickEventHandler: (VehicleOverviewControllerEvents) -> Unit) :
    TypedEpoxyController<Vehicle>() {
    override fun buildModels(data: Vehicle?) {

    }
}