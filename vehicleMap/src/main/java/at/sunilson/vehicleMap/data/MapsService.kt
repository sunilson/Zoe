package at.sunilson.vehicleMap.data

import at.sunilson.vehicleMap.data.models.ReachableAreaPostBody
import at.sunilson.vehicleMap.data.models.ReachableAreaResponse
import at.sunilson.vehicleMap.data.models.WeatherResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MapsService {

    @GET("https://api.openweathermap.org/data/2.5/weather?units=metric")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @POST("https://bemap-myr.benomad.com/myrenaultservices/journey/v1.0/reachablearea")
    suspend fun getReachableArea(@Body body: ReachableAreaPostBody): ReachableAreaResponse

}