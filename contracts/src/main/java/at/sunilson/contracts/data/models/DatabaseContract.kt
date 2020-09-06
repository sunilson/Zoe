package at.sunilson.contracts.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class DatabaseContract(
    @PrimaryKey
    val id: String,
    val vin: String,
    val type: String,
    val durationMonths: Int,
    val startDate: String?,
    val endDate: String?,
    val status: String,
    val statusLabel: String,
    val description: String,
    val maximumMileage: Int?,
    val mileageUnit: String?
)