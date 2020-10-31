package at.sunilson.vehicleMap.domain.entities

import com.google.android.libraries.maps.model.LatLng

data class GetChargingStationsParams(val location: LatLng, val radius: Double)