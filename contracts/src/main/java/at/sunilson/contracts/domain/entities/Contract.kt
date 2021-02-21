package at.sunilson.contracts.domain.entities

import java.time.LocalDate

data class Contract(
    val id: String,
    val type: String,
    val durationMonths: Int,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val status: String,
    val statusLabel: String,
    val description: String,
    val maximumMileage: Int?,
    val mileageUnit: String?
)
