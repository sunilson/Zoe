package at.sunilson.vehicleMap.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
internal data class WeatherResponse(val main: WeatherMain)

@JsonClass(generateAdapter = true)
@Keep
internal data class WeatherMain(val temp: Double)
