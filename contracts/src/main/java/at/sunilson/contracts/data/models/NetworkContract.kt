package at.sunilson.contracts.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkContract(
    val type: String,
    val contractId: String?,
    val code: String,
    val durationMonths: Int,
    val startDate: String?,
    val endDate: String?,
    val status: String,
    val statusLabel: String,
    val description: String,
    val conditions: List<NetworkContractConditions>
)