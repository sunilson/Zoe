package at.sunilson.vehicleMap.domain

import at.sunilson.core.usecases.FlowUseCase
import at.sunilson.networkingcore.constants.ApiKeys.WEATHER_API_KEY
import at.sunilson.vehicleMap.data.MapsService
import at.sunilson.vehicleMap.data.models.ReachableAreaPostBody
import at.sunilson.vehicleMap.domain.entities.ReachableArea
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.domain.GetSelectedVehicle
import at.sunilson.vehiclecore.domain.entities.Location
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetReachableArea @Inject constructor(private val getVehicle: GetSelectedVehicle) : FlowUseCase<ReachableArea, Unit>() {

    @Inject
    internal lateinit var mapsService: MapsService

    private var previousBatteryLevel: Int? = null
    private var previousLocation: Location? = null
    private var previousTemperature: Int? = null

    override fun run(params: Unit) = getVehicle(Unit).map { vehicle ->
        val location = vehicle?.location ?: return@map null

        val weather =
            mapsService.getWeatherData(location.lat, location.lng, WEATHER_API_KEY)

        val isSameLocation = previousLocation == location
        val isSameBatteryLevel =
            previousBatteryLevel == vehicle.batteryStatus.batteryLevel
        val isSameTemperature = previousTemperature == weather.main.temp.toInt()

        previousTemperature = weather.main.temp.toInt()
        previousLocation = location
        previousBatteryLevel = vehicle.batteryStatus.batteryLevel

        if (isSameBatteryLevel && isSameLocation && isSameTemperature) {
            return@map null
        }

        val result = mapsService.getReachableArea(
            ReachableAreaPostBody(
                vehicle.vin.substring(0..7),
                vehicle.batteryStatus.batteryLevel,
                weather.main.temp.toInt(),
                location.lng,
                location.lat
            )
        )

        ReachableArea(
            LatLngBounds(
                LatLng(
                    result.boundingBox.minLat,
                    result.boundingBox.minLon
                ),
                LatLng(
                    result.boundingBox.maxLat,
                    result.boundingBox.maxLon
                )
            ),
            PolyUtil.decode(result.encodedGeometry)
        )
    }.filterNotNull()
}