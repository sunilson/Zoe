package at.sunilson.vehicleMap.data

import at.sunilson.networkingcore.constants.ApiKeys
import at.sunilson.vehicleMap.data.models.ChargingStationsResponse
import at.sunilson.vehicleMap.data.models.ReachableAreaPostBody
import at.sunilson.vehicleMap.data.models.ReachableAreaResponse
import at.sunilson.vehicleMap.data.models.WeatherResponse
import retrofit2.Response
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

    @GET("https://api.openchargemap.io/v3/poi")
    suspend fun getChargingStations(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("distance") radius: Double,
        @Query("distanceunit") distanceUnit: String = "KM",
        @Query("key") apiKey: String = ApiKeys.CHARGING_API_KEY
    ): List<ChargingStationsResponse>

    @POST("https://bemap-myr.benomad.com/myrenaultservices/journey/v1.0/reachablearea")
    suspend fun getReachableArea(@Body body: ReachableAreaPostBody): ReachableAreaResponse
}
