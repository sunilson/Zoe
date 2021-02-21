package at.sunilson.vehicleMap.data

import at.sunilson.vehicleMap.data.models.ChargingStationsResponse
import at.sunilson.vehicleMap.data.models.ReachableAreaPostBody
import at.sunilson.vehicleMap.data.models.ReachableAreaResponse
import at.sunilson.vehicleMap.data.models.WeatherResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface MapsService {

    @GET("https://api.openweathermap.org/data/2.5/weather?units=metric")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("https://mycms-bff-web-prod.apps.eu.kamereon.io/bff-web/v1/poi?type=0")
    suspend fun getChargingStations(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("radius") radius: Double
    ): List<ChargingStationsResponse>

    @POST("https://bemap-myr.benomad.com/myrenaultservices/journey/v1.0/reachablearea")
    suspend fun getReachableArea(@Body body: ReachableAreaPostBody): ReachableAreaResponse
}
