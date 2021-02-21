package at.sunilson.vehicleMap.domain

import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.libraries.maps.model.LatLng

val Location.position: LatLng
    get() = LatLng(lat, lng)
