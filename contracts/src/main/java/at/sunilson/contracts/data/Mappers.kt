package at.sunilson.contracts.data

import at.sunilson.contracts.data.models.DatabaseContract
import at.sunilson.contracts.data.models.NetworkContract
import at.sunilson.contracts.domain.entities.Contract
import java.time.LocalDate

internal fun NetworkContract.toDatabaseEntity(vin: String) = DatabaseContract(
    contractId ?: code,
    vin,
    type,
    durationMonths,
    startDate,
    endDate,
    status,
    statusLabel,
    description,
    if (conditions.firstOrNull()?.unlimitedMileage == true) {
        null
    } else {
        conditions.firstOrNull()?.maximumMileage
    },
    conditions.firstOrNull()?.mileageUnit
)

internal fun DatabaseContract.toEntity() = Contract(
    id,
    type,
    durationMonths,
    if (startDate == null) null else LocalDate.parse(startDate),
    if (endDate == null) null else LocalDate.parse(endDate),
    status,
    status,
    description,
    maximumMileage,
    mileageUnit
)
