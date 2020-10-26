package at.sunilson.vehicle.presentation.vehicleOverview.epxoy.models

import android.widget.TextView
import at.sunilson.ktx.datetime.toZonedDateTime
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatFull
import at.sunilson.vehicle.R
import at.sunilson.vehiclecore.domain.entities.Location
import at.sunilson.vehiclecore.domain.entities.Vehicle
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions

@EpoxyModelClass
abstract class VehicleLocationWidgetModel :
    EpoxyModelWithHolder<VehicleLocationWidgetModel.Holder>() {

    override fun getDefaultLayout() = R.layout.vehicle_location_widget

    @EpoxyAttribute
    lateinit var vehicle: Vehicle

    @EpoxyAttribute
    var location: Location? = null

    @EpoxyAttribute(hash = false)
    lateinit var onMapClick: (String) -> Unit

    private var previousMarker: Marker? = null

    override fun bind(holder: Holder) {
        holder.run {
            timestamp.text =
                location?.timestamp?.toZonedDateTime()?.formatFull() ?: timestamp.context.getString(R.string.nothing)

            map.onCreate(null)
            map.getMapAsync { googleMap ->
                googleMap.uiSettings.isMapToolbarEnabled = false
                googleMap.uiSettings.isMyLocationButtonEnabled = false
                googleMap.uiSettings.isCompassEnabled = false
                googleMap.setOnMapClickListener { onMapClick(vehicle.vin) }
                location?.let {
                    previousMarker?.remove()
                    previousMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.lat, it.lng))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.zoe))
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lng)))
                }
            }
        }
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        previousMarker?.remove()
        previousMarker = null
    }

    class Holder : KotlinEpoxyHolder() {
        val map by bind<MapView>(R.id.map)
        val timestamp by bind<TextView>(R.id.timestamp)
    }
}