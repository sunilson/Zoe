package at.sunilson.contracts.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkContractConditions(
    val startDate: String? = null,
    val endDate: String? = null,
    val unlimitedMileage: Boolean? = null,
    val maximumMileage: Int? = null,
    val mileageUnit: String? = null
)