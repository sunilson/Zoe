package at.sunilson.vehicleMap.domain.entities

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

data class ReachableArea(val boundingBox: LatLngBounds, val coordinates: List<LatLng>)