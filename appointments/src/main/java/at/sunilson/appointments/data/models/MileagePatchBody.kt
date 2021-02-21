package at.sunilson.appointments.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class MileagePatchBody(
    val value: Int,
    val op: String = "replace",
    val path: String = "annualMileage"
)
