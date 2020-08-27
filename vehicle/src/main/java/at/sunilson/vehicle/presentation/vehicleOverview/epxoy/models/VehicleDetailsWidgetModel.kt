package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.Button
import android.widget.TextView
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatFull
import at.sunilson.vehicle.R
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class VehicleDetailsWidgetModel :
    EpoxyModelWithHolder<VehicleDetailsWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_details_widget

    @EpoxyAttribute
    lateinit var vehicle: Vehicle

    @EpoxyAttribute
    lateinit var onButtonClick: (String) -> Unit

    override fun bind(holder: Holder) = holder.run {
        vehicleTimestamp.text = "${vehicle.lastChangeTimestamp.toZonedDateTime().formatFull()}"
        mileageText.text = "Kilometerstand: ${vehicle.mileageKm} Km"
        vehicleName.text = vehicle.modelName
        detailsButton.setOnClickListener { onButtonClick(vehicle.vin) }
        estimatedRangeText.text =
            "Reichweite ${vehicle.batteryStatus.remainingRange} km (${vehicle.batteryStatus.availableEnery} kWh)"
    }

    class Holder : KotlinEpoxyHolder() {
        val vehicleName by bind<TextView>(R.id.headline)
        val mileageText by bind<TextView>(R.id.vehicle_mileage)
        val vehicleTimestamp by bind<TextView>(R.id.vehicle_timestamp)
        val estimatedRangeText by bind<TextView>(R.id.vehicle_estimated_range)
        val detailsButton by bind<Button>(R.id.vehicle_details_button)
    }
}